package cn.itcast.core.service.brand;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import cn.itcast.core.pojo.good.Goods;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    //    @Autowired
    // 好处：
    // 1、提高框架的性能
    // 2、降低与框架间的耦合度
    @Resource
    private BrandDao brandDao;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @Override
    public List<Brand> findAll() {
        List<Brand> brands = brandDao.selectByExample(null);
        return brands;
    }

    /**
     * 品牌列表分页查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        // 1、设置分页条件-分页插件
        PageHelper.startPage(pageNo, pageSize);
        // 2、根据条件查询
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(null);
        // 3、创建PageResult对象并且封装结果
        return new PageResult(page.getResult(), page.getTotal());
    }

    /**
     * 品牌列表条件查询
     *
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */
    @Override
    public PageResult search(Integer pageNo, Integer pageSize, Brand brand) {
        // 1、设置分页条件-分页插件
        PageHelper.startPage(pageNo, pageSize);
        // 2、设置查询条件
        BrandQuery brandQuery = new BrandQuery();
        // 封装查询条件：其始就是在给我们拼接查询条件
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if (brand.getName() != null && !"".equals(brand.getName().trim())) {
            // select id,name,first_char from tb_brand where name like "%"?"%"
            criteria.andNameLike("%" + brand.getName().trim() + "%");
        }
        if (brand.getFirstChar() != null && !"".equals(brand.getFirstChar().trim())) {
            // select id,name,first_char from tb_brand where name like "%"?"%" and first_char = ?
            criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
        }
        if (brand.getAuditStatus() != null && !"".equals(brand.getAuditStatus().trim())) {
            // select id,name,first_char from tb_brand where name like "%"?"%" and first_char = ?
            criteria.andAuditStatusEqualTo(brand.getAuditStatus().trim());
        }
        // 设置根据字段排序
        // select id,name,first_char from tb_brand where name like "%"?"%" and first_char = ? order by id desc
        brandQuery.setOrderByClause("id desc");
        // 3、根据条件查询
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        // 4、创建PageResult对象并且封装结果
        return new PageResult(page.getResult(), page.getTotal());
    }

    /**
     * 新增品牌
     *
     * @param brand
     */
    @Transactional
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }

    /**
     * 回显品牌
     *
     * @param id
     * @return
     */
    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    /**
     * 更新品牌
     *
     * @param brand
     */
    @Transactional
    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    /**
     * 品牌批量删除
     *
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if (ids != null && ids.length > 0) {
//            for (Long id : ids) {
//                brandDao.deleteByPrimaryKey(id);
//            }
            // 批量删除-自定义
            brandDao.deleteByPrimaryKeys(ids);
        }
    }

    /**
     * 新增规格时初始化品牌列表
     *
     * @return
     */
    @Override
    public List<Map<String, String>> selectOptionList() {
        return brandDao.selectOptionList();
    }

    /**
     * 品牌审核
     *
     * @param ids
     * @param status
     */
    @Transactional
    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids != null && ids.length > 0) {
            Brand brand = new Brand();
            brand.setAuditStatus(status);
            for (final Long id : ids) {
                brand.setId(id);
                //更新商品审核状态
                brandDao.updateByPrimaryKeySelective(brand);
            }
        }
    }
}
