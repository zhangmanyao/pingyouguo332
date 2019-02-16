package cn.itcast.core.controller.user;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.order.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @RequestMapping("/findOrderByUserid.do")
    public List<Order> findOrderByUserid(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Order> orders = orderService.findOrderByUserid(userId);
        for (Order order : orders) {
            System.out.println(order);
        }
        return orders;
    }
}
