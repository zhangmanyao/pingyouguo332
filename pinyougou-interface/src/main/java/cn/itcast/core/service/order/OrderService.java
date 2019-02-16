package cn.itcast.core.service.order;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;

public interface OrderService {

    /**
     * 保存订单
     * @param username
     * @param order
     */
    public void add(String username, Order order);

    void updateOrderStatus(Long[] ids, String status);

    PageResult searchForShop(Integer page, Integer rows, Order order);
}
