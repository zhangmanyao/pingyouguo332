package cn.itcast.core.controller.order;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.order.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    /**
     *  运营商后台-订单统计
     * @param page
     * @param rows
     * @param createTime
     * @return
     */
    @RequestMapping("/statistics.do")
    public PageResult search(Integer page, Integer rows, String createTime, @RequestBody Order order){
        System.out.println(createTime);
        Date startDate = null;
        Date endDate = null;
        if (createTime!=null&&!"".equals(createTime)){
            startDate = new Date();
            endDate = new Date();
            if ("1".equals(createTime)){
                startDate.setHours(startDate.getHours()-24);
            }else
            if ("2".equals(createTime)){
                startDate.setDate(startDate.getDate()-7);
            }else
            if ("3".equals(createTime)){
                startDate.setMonth(startDate.getMonth()-1);
            }else
            if ("4".equals(createTime)){
                startDate.setMonth(startDate.getMonth()-3);
            }else
            if ("5".equals(createTime)){
                startDate.setMonth(startDate.getMonth()-6);
            }else
            if ("6".equals(createTime)){
                startDate.setMonth(startDate.getMonth()-12);
            }else
            if ("7".equals(createTime)){
                startDate.setMonth(startDate.getMonth()-12);
                endDate.setMonth(endDate.getMonth()-12);
            }
        }
        System.out.println(startDate);
        System.out.println(endDate);
        return orderService.statistics(page, rows, startDate,endDate,order);
    }
    /**
     *  运营商后台-订单查询
     * @param page
     * @param rows
     * @param minPrice
     * @param maxPrice
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, String minPrice ,String maxPrice , @RequestBody Order order){
        return orderService.search(page, rows, minPrice,maxPrice,order);
    }
}
