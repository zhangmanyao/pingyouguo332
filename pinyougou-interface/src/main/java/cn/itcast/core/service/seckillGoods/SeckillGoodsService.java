package cn.itcast.core.service.seckillGoods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.seckill.SeckillGoods;

public interface SeckillGoodsService {

    /**
     * 运营系统查询待审核的秒杀商品列表
     * @param page
     * @param rows
     * @param seckillGoods
     * @return
     */
    public PageResult searchForManager(Integer page, Integer rows, SeckillGoods seckillGoods);

    /**
     * 审核秒杀商品
     * @param ids
     * @param status
     */
    public void updateStatus(Long[] ids, String status);
}
