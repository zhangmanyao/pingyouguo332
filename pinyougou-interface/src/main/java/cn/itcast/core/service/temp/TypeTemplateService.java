package cn.itcast.core.service.temp;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {

    /**
     * 商品模板的列表查询
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate);

    /**
     * 新增模板
     * @param typeTemplate
     */
    public void add(TypeTemplate typeTemplate);

    /**
     * 通过模板加载对应的品牌以及扩展属性
     * @param id
     * @return
     */
    public TypeTemplate findOne(Long id);

    /**
     * 通过模板加载对应的规格以及规格选项
     * @param id
     * @return
     */
    public List<Map> findBySpecList(Long id);

    /**
     * 新增分类时获取模板的下拉框列表
     * @return
     */
    public List<TypeTemplate> findAll();

    /**
     * 模板审核
     * @param ids
     * @param status
     */
    void updateStatus(Long[] ids, String status);
}
