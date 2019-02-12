package cn.itcast.core.service.spec;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import cn.itcast.core.vo.SpecVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SpecServiceImpl implements SpecService {

    @Resource
    private SpecificationDao specificationDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    /**
     * 列表查询
     * @param page
     * @param rows
     * @param specification
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {
        // 1、设置分页条件
        PageHelper.startPage(page, rows);
        // 2、设置查询条件
        SpecificationQuery specificationQuery = new SpecificationQuery();
        if(specification.getSpecName() != null && !"".equals(specification.getSpecName().trim())){
            specificationQuery.createCriteria().andSpecNameLike("%" + specification.getSpecName().trim() +"%");
        }
        specificationQuery.setOrderByClause("id desc");
        // 3、根据条件查询
        Page<Specification> p = (Page<Specification>) specificationDao.selectByExample(specificationQuery);
        // 4、将结果封装到PageResult中
        return new PageResult(p.getResult(), p.getTotal());
    }

    /**
     * 新增规格
     * @param specVo
     */
    @Transactional
    @Override
    public void add(SpecVo specVo) {
        // 保存规格
        Specification specification = specVo.getSpecification();
        specificationDao.insertSelective(specification);    // 配置返回自增主键id
        // 保存规格选项-外键-规格id
        List<SpecificationOption> specificationOptionList = specVo.getSpecificationOptionList();
        if(specificationOptionList != null && specificationOptionList.size() > 0){
            for (SpecificationOption specificationOption : specificationOptionList) {
                // 设置外键
                specificationOption.setSpecId(specification.getId());
//                specificationOptionDao.insertSelective(specificationOption);
            }
            // 批量插入
            specificationOptionDao.insertSelectives(specificationOptionList);
        }
    }

    /**
     * 回显规格
     * @param id
     * @return
     */
    @Override
    public SpecVo findOne(Long id) {
        // 创建vo用来封装数据
        SpecVo specVo = new SpecVo();
        // 查询规格
        Specification specification = specificationDao.selectByPrimaryKey(id);
        specVo.setSpecification(specification);
        // 查询规格选项
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        specificationOptionQuery.createCriteria().andSpecIdEqualTo(id);
        List<SpecificationOption> specificationOptionList = specificationOptionDao.selectByExample(specificationOptionQuery);
        specVo.setSpecificationOptionList(specificationOptionList);
        return specVo;
    }

    /**
     * 更新规格
     * @param specVo
     */
    @Transactional
    @Override
    public void update(SpecVo specVo) {
        // 更新规格
        Specification specification = specVo.getSpecification();
        specificationDao.updateByPrimaryKeySelective(specification);
        // 更新规格选项
        // 先删除
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        specificationOptionQuery.createCriteria().andSpecIdEqualTo(specification.getId());
        specificationOptionDao.deleteByExample(specificationOptionQuery);
        // 再插入
        List<SpecificationOption> specificationOptionList = specVo.getSpecificationOptionList();
        if(specificationOptionList != null && specificationOptionList.size() > 0){
            for (SpecificationOption specificationOption : specificationOptionList) {
                // 设置外键
                specificationOption.setSpecId(specification.getId());
//                specificationOptionDao.insertSelective(specificationOption);
            }
            // 批量插入
            specificationOptionDao.insertSelectives(specificationOptionList);
        }
    }

    /**
     * 批量删除规格
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if(ids != null && ids.length > 0){
            for (Long id : ids) {
                // 先删除规格选项
                SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
                specificationOptionQuery.createCriteria().andSpecIdEqualTo(id);
                specificationOptionDao.deleteByExample(specificationOptionQuery);
                // 再删除规格
                specificationDao.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 新增模板时初始化规格列表
     * @return
     */
    @Override
    public List<Map<String, String>> selectOptionList() {
        return specificationDao.selectOptionList();
    }
}
