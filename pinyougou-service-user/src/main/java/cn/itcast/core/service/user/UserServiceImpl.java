package cn.itcast.core.service.user;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.utils.md5.MD5Util;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination smsDestination;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserDao userDao;

    /**
     * 获取短信验证码
     * @param phone
     */
    @Override
    public void sendCode(final String phone) {
        // 随机生成6位数的短信验证码
        final String code = RandomStringUtils.randomNumeric(6);
        System.out.println("code:"+code);
        // 将验证码存储
        redisTemplate.boundValueOps(phone).set(code);
        // 设置该验证码的过期时间
        redisTemplate.boundValueOps(phone).expire(5, TimeUnit.MINUTES);
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // 发送的数据：手机号、验证码、签名、模板
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("phoneNumbers", phone);
                mapMessage.setString("signName", "阮文");
                mapMessage.setString("templateCode", "SMS_140720901");
                mapMessage.setString("templateParam", "{\"code\":\""+code+"\"}");
                return mapMessage;
            }
        });
    }

    /**
     * 用户注册
     * @param smscode
     * @param user
     */
    @Override
    public void add(String smscode, User user) {
        // 首先需要去校验验证码是否正确
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if(code != null && smscode != null && !"".equals(smscode) && code.equals(smscode)){
            // 用户密码需要加密：md5加密
            String password = MD5Util.MD5Encode(user.getPassword(), "");
            user.setPassword(password);
            user.setCreated(new Date());
            user.setUpdated(new Date());
            userDao.insertSelective(user);
        }else{
            throw new RuntimeException("验证码不正确");
        }
    }

    @Override
    public PageResult search(Integer page, Integer rows, User user) {
        return null;
    }

    @Override
    public void updateStatus(String userId, String status) {

    }
}
