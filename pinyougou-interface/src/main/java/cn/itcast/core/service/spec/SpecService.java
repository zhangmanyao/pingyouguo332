package cn.itcast.core.service.spec;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.vo.SpecVo;

import java.util.List;
import java.util.Map;

public interface SpecService {

    /**
     * 规格列表查询
     * @param page
     * @param rows
     * @param specification
     * @return
     */
    public PageResult search(Integer page, Integer rows, Specification specification);

    /**
     * 新增规格
     * @param specVo
     */
    public void add(SpecVo specVo);

    /**
     * 回显规格
     * @param id
     * @return
     */
    public SpecVo findOne(Long id);

    /**
     * 更新规格
     * @param specVo
     */
    public void update(SpecVo specVo);

    /**
     * 批量删除规格
     * @param ids
     */
    public void delete(Long[] ids);


    /**
     * 新增模板时初始化规格列表
     * @return
     */
    public List<Map<String, String>> selectOptionList();
}
