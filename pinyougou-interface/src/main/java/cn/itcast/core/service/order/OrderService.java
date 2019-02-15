package cn.itcast.core.service.order;

import cn.itcast.core.pojo.order.Order;

import java.util.List;

public interface OrderService {

    /**
     * 保存订单
     * @param username
     * @param order
     */
    public void add(String username, Order order);

    List<Order> findOrderByUserid(String userId);
}
