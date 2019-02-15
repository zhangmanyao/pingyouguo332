package cn.itcast.core.service.seckillOrder;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.seckill.SeckillOrder;

public interface SeckillOrderService {


    public PageResult searchForShop(Integer page, Integer rows, SeckillOrder seckillOrder);

    PageResult searchForShop1(Integer page, Integer rows, SeckillOrder seckillOrder);
}

