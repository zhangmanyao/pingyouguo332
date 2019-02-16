package cn.itcast.core.service.seckillGoods;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Resource
    private SeckillGoodsDao seckillGoodsDao;

    /**
     * 运营系统查询待审核的商品列表
     * @param page
     * @param rows
     * @param seckillGoods
     * @return
     */
    @Override
    public PageResult searchForManager(Integer page, Integer rows, SeckillGoods seckillGoods) {
        // 设置分页条件
        PageHelper.startPage(page, rows);
        // 设置查询条件：待审核
        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = seckillGoodsQuery.createCriteria();
        if(seckillGoods.getStatus()!= null && !"".equals(seckillGoods.getStatus().trim())){
            criteria.andStatusEqualTo(seckillGoods.getStatus().trim());
        }
        // 根据条件查询
        Page<SeckillGoods> p = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(seckillGoodsQuery);
        // 封装结果
        return new PageResult(p.getResult(), p.getTotal());
    }

    /**
     * 审核商品
     * @param ids
     * @param status
     */
    @Transactional
    @Override
    public void updateStatus(Long[] ids, String status) {
        if(ids != null && ids.length > 0){
            SeckillGoods seckillGoods = new SeckillGoods();
            seckillGoods.setStatus(status);
            for (Long id : ids) {
                seckillGoods.setId(id);
                //更新商品审核状态
                seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
            }
        }
    }
}
