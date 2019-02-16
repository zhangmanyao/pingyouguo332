package cn.itcast.core.service.itemcat;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

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

<<<<<<< HEAD
=======
    /**
     * 分类审核
     * @param ids
     * @param status
     */
    @Transactional
    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids != null && ids.length > 0) {
            ItemCat itemCat = new ItemCat();
            itemCat.setAuditStatus(status);
            for (final Long id : ids) {
                itemCat.setId(id);
                //更新商品审核状态
                itemCatDao.updateByPrimaryKeySelective(itemCat);
            }
        }
    }
>>>>>>> 2079a3bb234316c222fbeeb946c7c84205d7973b
}
