package cn.itcast.core.service.user;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.user.User;

public interface UserService {

    /**
     * 获取短信验证码
     * @param phone
     */
    public void sendCode(String phone);

    /**
     * 用户注册
     * @param smscode
     * @param user
     */
    public void add(String smscode, User user);

    /**
     * 回显全部用户
     * @param page
     * @param rows
     * @param user
     * @return
     */
    public PageResult search(Integer page, Integer rows,User user);

    /**
     * 审核用户状态
     * @param userId
     * @param status
     */
    void updateStatus(String userId, String status);
}
