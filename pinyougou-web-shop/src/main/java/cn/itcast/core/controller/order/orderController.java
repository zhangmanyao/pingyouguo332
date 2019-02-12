package cn.itcast.core.controller.order;

import cn.itcast.core.entity.Result;
import cn.itcast.core.service.order.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class orderController {
    @Reference
    OrderService orderService;
    @RequestMapping("/updateOrderStatus")
    public Result updateOrderStatus(Long orderId){
//git
        try {
            orderService.updateOrderStatus(orderId);
            return new Result(true,"订单已发货");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"订单状态修改失败");
        }

    }
}
