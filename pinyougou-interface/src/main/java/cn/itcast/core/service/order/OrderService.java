package cn.itcast.core.service.order;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;

import java.util.List;
import java.util.Date;

public interface OrderService {

    /**
     * 保存订单
     * @param username
     * @param order
     */
    public void add(String username, Order order);

    void updateOrderStatus(Long[] ids, String status);

    PageResult searchForShop(Integer page, Integer rows, Order order);

    List<Order> findOrderByUserid(String userId);

    /**
     * 待审核的商家的统计
     * @param page
     * @param rows
     * @param startDate
     * @param endDate
     * @return
     */
    public PageResult statistics(Integer page, Integer rows, Date startDate,Date endDate,Order order);

    /**
     * 运营商系统订单查询
     * @param page
     * @param rows
     * @param minPrice
     * @param maxPrice
     * @param order
     * @return
     */
    PageResult search(Integer page, Integer rows, String minPrice, String maxPrice, Order order);

}
