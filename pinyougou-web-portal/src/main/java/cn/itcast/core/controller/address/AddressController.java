package cn.itcast.core.controller.address;


import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.address.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    /**
     * 加载当前收货人的地址列表
     *
     * @return
     */
    @RequestMapping("/findListByLoginUser.do")
    public List<Address> findListByLoginUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.findListByLoginUser(userId);
    }
    @RequestMapping("/add.do")
    public Result add(@RequestBody Address address){
        try {
           addressService.add(address);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try{
            addressService.delete(ids);
            return new Result(true, "删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
    @RequestMapping("/update.do")
    public Result update(@RequestBody Address address){
        try {
            addressService.update(address);
            return new Result(true, "更新成功");
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }
}
