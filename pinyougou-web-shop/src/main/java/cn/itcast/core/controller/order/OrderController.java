package cn.itcast.core.controller.order;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.order.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    OrderService orderService;
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Order order){
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(sellerId);
        PageResult pageResult = orderService.searchForShop(page, rows, order);
        return pageResult;
    }
   /* @RequestMapping("/findPage")
    public PageResult findPage(Integer page, Integer rows){
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Order order = new Order();
        order.setSellerId(sellerId);
        return orderService.searchForShop(page, rows, order);
    }*/

    /**
     * 修改订单状态
     * @param ids
     * @param status
     * @return
     */
    @RequestMapping("/updateStatus")
    public Result updateOrderStatus(Long[] ids,String status){
        try {
            orderService.updateOrderStatus(ids,status);
            return new Result(true,"订单已发货");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"订单状态修改失败");
        }

    }
}
