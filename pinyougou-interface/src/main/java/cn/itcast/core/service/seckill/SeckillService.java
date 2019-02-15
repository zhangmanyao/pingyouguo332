package cn.itcast.core.service.seckill;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.vo.GoodsVo;

public interface SeckillService {


    public PageResult searchForShop(Integer page, Integer rows, SeckillGoods seckillGoods);

    PageResult findPage(Integer page, Integer rows);
}