package cn.itcast.core.controller.itemcat;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.itemcat.ItemCatListService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "itemCat")
public class ItemcatConteroller {

    @Reference
    private ItemCatListService itemCatListService;

    /**
     * 查询商品分类信息
     *
     * @return
     */
    @RequestMapping(value = "findItemCatList.do")
    public List<ItemCat> findItemCatList() {
        List<ItemCat> itemCatList = itemCatListService.findItemCatList();
        return itemCatList;
    }

}
