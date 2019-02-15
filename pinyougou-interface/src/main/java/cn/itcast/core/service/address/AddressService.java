package cn.itcast.core.service.address;

import cn.itcast.core.pojo.address.Address;

import java.util.List;

public interface AddressService {

    /**
     * 加载当前收货人的地址列表
     * @param userId
     * @return
     */
    public List<Address> findListByLoginUser(String userId);

    /*增加收货人地址*/
    public void add(Address address);
    /*删除收货人地址*/
    public void delete(Long[] ids);
    /*更改收货人地址*/
    public void update(Address address);

}
