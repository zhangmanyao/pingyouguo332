package cn.itcast.core.service.seckill;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;
import java.util.List;

@Service
class SeckillServiceImpl implements SeckillService {

    @Resource
    private SeckillGoodsDao seckillGoodsDao;

    @Override
    public PageResult searchForShop(Integer page, Integer rows, SeckillGoods seckillGoods) {
        PageHelper.startPage(page, rows);
        // 设置查询条件：待审核并且是未删除
        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = seckillGoodsQuery.createCriteria();
        if (seckillGoods.getStatus() != null && !"".equals(seckillGoods.getStatus().trim())) {
            criteria.andStatusEqualTo(seckillGoods.getStatus().trim());
        }
        criteria.andStatusEqualTo("0");
        seckillGoodsQuery.setOrderByClause("id desc");
        // 根据条件查询
        Page<SeckillGoods> p = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(seckillGoodsQuery);
        // 封装结果
        return new PageResult(p.getResult(), p.getTotal());
    }

    @Override
    public PageResult findPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        Page<SeckillGoods> seckillGoods = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(null);
        return  new PageResult(seckillGoods.getResult(),seckillGoods.getTotal());
    }
}
