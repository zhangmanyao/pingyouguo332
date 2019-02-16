package cn.itcast.core.service.order;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.utils.uniquekey.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private IdWorker idWorker;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ItemDao itemDao;

    @Resource
    private PayLogDao payLogDao;

    /**
     * 商家查询订单
     * @param pageNo
     * @param pageSize
     * @param order
     * @return
     */
    @Override
    public PageResult searchForShop(Integer pageNo, Integer pageSize, Order order) {
        PageHelper.startPage(pageNo,pageSize);
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        //封装根据订单状态查询条件
        if(order.getStatus()!=null&&!"".equals(order.getStatus().trim())){
            criteria.andStatusEqualTo(order.getStatus());
        }
        //封装根据订单编号查询条件
        if(order.getOrderId()!=null&&!"".equals(order.getOrderId().toString().trim())){
            criteria.andOrderIdEqualTo(order.getOrderId());
        }
        //封装根据用户名查询条件
        if(order.getUserId()!=null&&!"".equals(order.getUserId().trim())){

            criteria.andUserIdEqualTo(order.getUserId());
        }
        //封装根据商家id查询条件
        if(order.getSellerId()!=null&&!"".equals(order.getSellerId().trim())){
            criteria.andSellerIdEqualTo(order.getSellerId());
        }
        orderQuery.setOrderByClause("create_time desc");
        Page<Order> page = (Page<Order>)orderDao.selectByExample(orderQuery);
        return new PageResult(page.getResult(),page.getTotal());
    }

    @Override
    public List<Order> findOrderByUserid(String userId) {
        return null;
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long[] ids,String status) {
        if (ids!=null&&ids.length>0){
            Order order = new Order();
            order.setStatus(status);
            for (Long id : ids) {
                order.setOrderId(id);
                 orderDao.updateByPrimaryKeySelective(order);

                 }
        }





    }

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination topicClosepayDestination;

    /**
     * 保存订单
     * @param username
     * @param order
     */
    @Override
    public void add(String username, final Order order) {
        // 保存订单：以商家为单位
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("BUYER_CART").get(username);
        if(cartList != null && cartList.size() > 0){
            double logTotalFee = 0f;
            List<Long> orderList = new ArrayList<>();   // 保存订单号
            for (Cart cart : cartList) {
                final long orderId = idWorker.nextId();
                orderList.add(orderId);
                order.setOrderId(orderId);  // 订单主键
                double payment = 0f;        // 该商家下的订单总金额
                order.setPaymentType("1");  // 支付类型：在线支付
                order.setStatus("1");       // 支付状态：待付款
                order.setCreateTime(new Date());
                order.setUserId(username);  // 下单的用户
                order.setSourceType("2");   // 订单来源：pc端
                order.setSellerId(cart.getSellerId());  // 商家id

                List<OrderItem> orderItemList = cart.getOrderItemList();
                if(orderItemList != null && orderItemList.size() > 0){
                    for (OrderItem orderItem : orderItemList) {
                        long id = idWorker.nextId();
                        orderItem.setId(id);    // 订单明细主键
                        orderItem.setOrderId(orderId);  // 外键
                        Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                        orderItem.setTitle(item.getTitle());    // 商品标题
                        orderItem.setPrice(item.getPrice());    // 商品单价
                        orderItem.setPicPath(item.getImage());  // 商品图片
                        orderItem.setSellerId(cart.getSellerId());  // 商家id
                        double totalFee = item.getPrice().doubleValue() * orderItem.getNum();
                        orderItem.setTotalFee(new BigDecimal(totalFee));
                        payment += totalFee;    // 该商家下的订单总金额

                        // 保存订单明细
                        orderItemDao.insertSelective(orderItem);
                    }
                }
                logTotalFee += payment;
                order.setPayment(new BigDecimal(payment));
                // 保存订单
                orderDao.insertSelective(order);

                //提交订单后将订单放入mq中，等待5m后未付款则关闭订单
                jmsTemplate.send(topicClosepayDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        // 将商品的id封装成消息体进行发送
                        TextMessage textMessage = session.createTextMessage(String.valueOf(orderId));
                        return textMessage;
                    }
                });
            }

            // 订单提交成功后，需要生成交易日志
            PayLog payLog = new PayLog();
            payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));    // 支付日志的交易流水号
            payLog.setCreateTime(new Date());                           // 生成日志的时间
            payLog.setTotalFee((long)logTotalFee*100);               // 支付的金额：本次订单的所有的金额
            payLog.setUserId(username); // 当前订单的用户
            payLog.setOrderList(orderList.toString().replace("[","").replace("]", ""));  // 栗子：[123,456]
            payLog.setTradeState("0");  // 待支付
            payLog.setPayType("1"); // 在线支付
            payLogDao.insertSelective(payLog);
            // 将日志缓存起来
            redisTemplate.boundHashOps("paylog").put(username, payLog);
        }

        // 订单提交成功后，清空购物车
        redisTemplate.boundHashOps("BUYER_CART").delete(username);
    }

    /**
     * 运营商后台-订单统计
     * @param page
     * @param rows
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public PageResult statistics(Integer page, Integer rows, Date startDate,Date endDate,Order order) {
        // 1、设置分页条件
        PageHelper.startPage(page, rows);
        // 2、设置查询条件
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        // 订单创建时间段
        if (startDate!=null){
            if (!startDate.equals(endDate)){
                criteria.andCreateTimeBetween(startDate,endDate);
            }else if (startDate.equals(endDate)){
                criteria.andCreateTimeNotBetween(startDate,new Date());
            }
        }
        // 订单状态
        if (order.getStatus()!=null&&!"".equals(order.getStatus())){
            criteria.andStatusEqualTo(order.getStatus());
        }
        // 订单创建时间
        if (order.getCreateTime()!=null&&!"".equals(order.getCreateTime())){
            criteria.andCreateTimeEqualTo(order.getCreateTime());
        }
        // 3、根据条件查询
        List<Order> orders = orderDao.selectByExample(orderQuery);
        Page<Order> p = (Page<Order>) orders;
        // 4、封装结果

        return new PageResult(p.getResult(), p.getTotal());
    }

    @Override
    public PageResult search(Integer page, Integer rows, String minPrice, String maxPrice, Order order) {
        // 1、设置分页条件
        PageHelper.startPage(page, rows);
        // 2、设置查询条件
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        // 订单金额区间
        if (minPrice!=null&&!"".equals(minPrice)){
            criteria.andPaymentGreaterThan(new BigDecimal(Long.parseLong(minPrice)));
        }
        if (maxPrice!=null&&!"".equals(maxPrice)){
            criteria.andPaymentLessThan(new BigDecimal(Long.parseLong(maxPrice)));
        }
        // 订单状态
        if (order.getStatus()!=null&&!"".equals(order.getStatus())){
            criteria.andStatusEqualTo(order.getStatus());
        }
        // 订单店铺名称
        if (order.getSellerId()!=null&&!"".equals(order.getSellerId())){
            criteria.andSellerIdLike(order.getSellerId());
        }
        // 3、根据条件查询
        Page<Order> p = (Page<Order>) orderDao.selectByExample(orderQuery);
        // 4、封装结果
        return new PageResult(p.getResult(), p.getTotal());
    }
}
