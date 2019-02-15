package cn.itcast.core.service.SeckillOrderService;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import cn.itcast.core.service.seckillOrder.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Resource
    private SeckillOrderDao seckillOrderDao;

    @Override
    public PageResult searchForShop(Integer page, Integer rows, SeckillOrder seckillOrder) {

        // 1、设置分页条件-分页插件
        PageHelper.startPage(page, rows);
        // 2、根据条件查询
        SeckillOrderQuery seckillOrderQuery = new SeckillOrderQuery();
        seckillOrderQuery.createCriteria().andSellerIdEqualTo(seckillOrder.getSellerId());
        Page<SeckillOrder> resul = (Page<SeckillOrder>) seckillOrderDao.selectByExample(seckillOrderQuery);
        // 3、创建PageResult对象并且封装结果
        return new PageResult(resul.getResult(), resul.getTotal());
    }

    @Override
    public PageResult searchForShop1(Integer page, Integer rows, SeckillOrder seckillOrder) {
        PageHelper.startPage(page, rows);

        Page<SeckillOrder> resul = (Page<SeckillOrder>) seckillOrderDao.selectByExample(null);
        // 3、创建PageResult对象并且封装结果
        return new PageResult(resul.getResult(), resul.getTotal());
    }
}
