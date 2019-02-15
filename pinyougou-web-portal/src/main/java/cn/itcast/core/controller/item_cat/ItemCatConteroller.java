package cn.itcast.core.controller.item_cat;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.itemcat.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatConteroller {

    @Reference
    private ItemCatService itemCatService;

    /**
     * 查询商品分类信息
     *
     * @return
     */
    @RequestMapping("/findItemCatList.do")
    public List<ItemCat> findItemCatList() {
        List<ItemCat> itemCatList = itemCatService.findItemCatList();
        return itemCatList;
    }
}
