package cn.itcast.core.controller.seckill;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.goods.GoodsService;
import cn.itcast.core.service.seckill.SeckillService;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Reference
    private SeckillService seckillService;

    /*    *
         * 商品保存
         * @param goodsVo
         * @return
        @RequestMapping("/add.do")
        public Result add(@RequestBody GoodsVo goodsVo){
            try {
                // 设置当前商家的id
                String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
                goodsVo.getGoods().setSellerId(sellerId);
                goodsService.add(goodsVo);
                return new Result(true, "保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, "保存失败");
            }
        }*/
    @RequestMapping("/findPage.do")
    public PageResult findPage(Integer page, Integer rows) {
        return seckillService.findPage(page, rows);
    }



    /**
     * 秒杀系统的商品申请
     *
     * @param page
     * @param rows
     * @param seckillGoods
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody SeckillGoods seckillGoods) {
        // 设置当前的商家的id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        seckillGoods.setSellerId(sellerId);
        return seckillService.searchForShop(page, rows, seckillGoods);
    }
}
/*    *
     * 商品回显
     * @param id
     * @return

    @RequestMapping("/findOne.do")
    public GoodsVo findOne(Long id){
        return goodsService.findOne(id);
    }

    *
     * 商品更新
     * @param goodsVo
     * @return

    @RequestMapping("/update.do")
    public Result update(@RequestBody GoodsVo goodsVo){
        try {
            goodsService.update(goodsVo);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }
}*/
