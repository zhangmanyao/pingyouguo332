package cn.itcast.core.service.itemcat;

import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {

    /**
     * 商品分类的列表查询
     * @param parentId
     * @return
     */
    public List<ItemCat> findByParentId(Long parentId);

    /**
     * 通过分类加载出模板id
     * @param id
     * @return
     */
    public ItemCat findOne(Long id);

    /**
     * 查询所有的分类并且显示分类名称
     * @return
     */
    public List<ItemCat> findAll();

    /**
     * 新增分类
     * @param itemCat
     */
    public void add(ItemCat itemCat);
}
