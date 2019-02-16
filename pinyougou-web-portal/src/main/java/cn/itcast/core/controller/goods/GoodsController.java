package cn.itcast.core.controller.goods;

import cn.itcast.core.entity.Result;
import cn.itcast.core.service.cart.CartService;
import cn.itcast.core.service.goods.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private GoodsService goodsService;
    @RequestMapping("/collectGoodsToRedis")
    @CrossOrigin(origins = {"http://localhost:9005"})
     public Result collectGoodsToRedis(Long itemId){
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsService.collectGoodsToRedis(itemId,username);
            return new Result(true,"收藏成功");
        }catch (Exception e){
           return new Result(false,"商品收藏失败");

        }



    }
}
