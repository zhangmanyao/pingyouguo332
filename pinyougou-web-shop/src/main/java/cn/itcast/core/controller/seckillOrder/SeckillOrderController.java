package cn.itcast.core.controller.seckillOrder;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.seckillOrder.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    /**
     * 商品保存
     * @param goodsVo
     * @return
     */
   /* @RequestMapping("/add.do")
    public Result add(@RequestBody GoodsVo goodsVo){
        try {
            // 设置当前商家的id
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsVo.getGoods().setSellerId(sellerId);
            seckillOrderService.add(goodsVo);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }
*/
    /**
     * 商家系统下的商品列表查询
     * @param page
     * @param rows
     * @param seckillOrder
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody SeckillOrder seckillOrder){
        // 设置当前的商家的id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        seckillOrder.setSellerId(sellerId);
        return seckillOrderService.searchForShop(page, rows, seckillOrder);
    }

  /*  *//**
     * 商品回显
     * @param id
     * @return
     *//*
    @RequestMapping("/findOne.do")
    public GoodsVo findOne(Long id){
        return goodsService.findOne(id);
    }

    *//**
     * 商品更新
     * @param goodsVo
     * @return
     *//*
    @RequestMapping("/update.do")
    public Result update(@RequestBody GoodsVo goodsVo){
        try {
            goodsService.update(goodsVo);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }*/
}
