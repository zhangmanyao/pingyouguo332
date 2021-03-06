package cn.itcast.core.task;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class RedisTask {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private TypeTemplateDao typeTemplateDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    // 将分类的数据同步到缓存
    @Scheduled(cron = "00 47 18 11 1 *") // cron:指定该方法的执行时间
    public void autoItemCatToRedis(){
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        if(itemCats != null && itemCats.size() > 0){
            for (ItemCat itemCat : itemCats) {
                // 将分类名称---模板id存储到redis中
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
            }
        }
        System.out.println("定时器执行啦啦啦1");
    }

    // 将模板的数据同步到缓存
    @Scheduled(cron = "00 47 18 11 1 *") // cron:指定该方法的执行时间
    public void autoTemplateToRedis(){
        List<TypeTemplate> list = typeTemplateDao.selectByExample(null);
        if(list != null && list.size() > 0){
            for (TypeTemplate template : list) {
                // 将品牌结果集放入缓存
                String brandIds = template.getBrandIds();
                List<Map> brandList = JSON.parseArray(brandIds, Map.class);
                redisTemplate.boundHashOps("brandList").put(template.getId(), brandList);
                // 将规格结果集放入缓存
                List<Map> specList = findBySpecList(template.getId());
                redisTemplate.boundHashOps("specList").put(template.getId(), specList);
            }
        }
        System.out.println("定时器执行啦啦啦2");
    }

    public List<Map> findBySpecList(Long id) {
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        // specIds：json串
        // 栗子：[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        // 将json串转成对象：阿里fastjson
        List<Map> specList = JSON.parseArray(specIds, Map.class);
        // 通过规格获取到规格选项
        if(specList != null && specList.size() > 0){
            for (Map map : specList) {
                Long specId = Long.parseLong(map.get("id").toString());
                SpecificationOptionQuery optionQuery = new SpecificationOptionQuery();
                optionQuery.createCriteria().andSpecIdEqualTo(specId);
                List<SpecificationOption> options = specificationOptionDao.selectByExample(optionQuery);
                map.put("options", options);
            }
        }
        // 最终specList：[{"id":27,"text":"网络","options":options},{"id":32,"text":"机身内存"}]
        return specList;
    }
}
