import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.specification.Specification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class Demo01 {

    @Autowired
    BrandDao brandDao;
    @Autowired
    SpecificationDao specificationDao;
    @Autowired
    ItemCatDao itemCatDao;


    @Test
    public void test1(){
        List<Brand> brands = brandDao.selectByExample(null);
        for (Brand brand : brands) {
            System.out.println(brand);
        }
    }

    @Test
    public void test2(){
        List<Specification> specifications = specificationDao.selectByExample(null);
        for (Specification specification : specifications) {
            System.out.println(specification);
        }
    }

    @Test
    public void test3(){
        List<ItemCat> itemCatList = itemCatDao.selectByExample(null);
        for (ItemCat itemCat : itemCatList) {
            System.out.println(itemCat);
        }
    }
}
