package cn.itcast.core.service.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.vo.GoodsVo;

public interface GoodsService {

    /**
     * 保存商品
     * @param goodsVo
     */
    public void add(GoodsVo goodsVo);

    /**
     * 商家系统下的商品列表查询
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    public PageResult searchForShop(Integer page, Integer rows, Goods goods);

    /**
     * 回显商品
     * @param id
     * @return
     */
    public GoodsVo findOne(Long id);

    /**
     * 更新商品
     * @param goodsVo
     */
    public void update(GoodsVo goodsVo);

    /**
     * 运营系统查询待审核的商品列表
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    public PageResult searchForManager(Integer page, Integer rows, Goods goods);

    /**
     * 审核商品
     * @param ids
     * @param status
     */
    public void updateStatus(Long[] ids, String status);

    /**
     * 删除商品
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 商品上架
     * @param id
     * @param status
     */
    void isMarketable(String id, String status);
}
