package cn.itcast.core.service.itemcat;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 商品分类的列表查询
     * @param parentId
     * @return
     */
    @Override
    public List<ItemCat> findByParentId(Long parentId) {

        // 点击列表查询的时候将数据写到缓存中
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        if(itemCats != null && itemCats.size() > 0){
            for (ItemCat itemCat : itemCats) {
                // 将分类名称---模板id存储到redis中
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
            }
        }

        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        return itemCatDao.selectByExample(itemCatQuery);
    }

    /**
     * 通过分类加载出模板id
     * @param id
     * @return
     */
    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    /**
     * 查询所有的分类并且显示分类名称
     * @return
     */
    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }

    /**
     * 新增分类
     * @param itemCat
     */
    @Override
    public void add(ItemCat itemCat) {
        itemCatDao.insertSelective(itemCat);
    }

    /**
     * 查询商品分类信息
     * @return
     */
    @Override
    public List<ItemCat> findItemCatList() {
        //从缓存中查询首页商品分类
        List<ItemCat> itemCatList = (List<ItemCat>) redisTemplate.boundHashOps("itemCat").get("indexItemCat");

        //如果缓存中没有数据，则从数据库查询再存入缓存
        if(itemCatList==null){
            //查询出1级商品分类的集合
            ItemCatQuery itemCatQuery = new ItemCatQuery();
            itemCatQuery.createCriteria().andParentIdEqualTo(0L);
            List<ItemCat> itemCatList1 = itemCatDao.selectByExample(itemCatQuery);

            //遍历1级商品分类的集合
            for(ItemCat itemCat1:itemCatList1){
                //查询2级商品分类的集合(将1级商品分类的id作为条件)
                ItemCatQuery itemCatQuery2 = new ItemCatQuery();
                itemCatQuery2.createCriteria().andIdEqualTo(itemCat1.getId());
                List<ItemCat> itemCatList2 = itemCatDao.selectByExample(itemCatQuery2);
                //遍历2级商品分类的集合
                for(ItemCat itemCat2:itemCatList2){
                    //查询3级商品分类的集合(将2级商品分类的父id作为条件)
                    ItemCatQuery itemCatQuery3 = new ItemCatQuery();
                    itemCatQuery3.createCriteria().andIdEqualTo(itemCat2.getId());
                    List<ItemCat> itemCatList3 = itemCatDao.selectByExample(itemCatQuery3);
                    //将2级商品分类的集合封装到2级商品分类实体中
                    itemCat2.setItemCatList(itemCatList3);
                }
                /*到这一步的时候，3级商品分类已经封装到2级分类中*/
                //将2级商品分类的集合封装到1级商品分类实体中
                itemCat1.setItemCatList(itemCatList2);
            }
            //存入缓存
            redisTemplate.boundHashOps("itemCat").put("indexItemCat",itemCatList1);
            return itemCatList1;
        }
        //到这一步，说明缓存中有数据，直接返回
        return itemCatList;
    }
}
