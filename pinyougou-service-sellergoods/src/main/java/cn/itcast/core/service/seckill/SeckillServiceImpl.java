package cn.itcast.core.service.seckill;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        if (seckillGoods.getTitle() != null && !"".equals(seckillGoods.getTitle() .trim())) {
            criteria.andStatusEqualTo(seckillGoods.getTitle() .trim());
        }
        criteria.andSellerIdEqualTo(seckillGoods.getSellerId());
        criteria.andStatusEqualTo("0");
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

    @Transactional
    @Override
    public void add(SeckillGoods seckillGoods) {
        seckillGoods.setCreateTime(new Date());
        seckillGoods.setStartTime(new Date());
        seckillGoods.setEndTime(new Date());
        seckillGoods.setStatus("0");
        seckillGoodsDao.insertSelective(seckillGoods);

    }


}
