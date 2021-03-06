package cn.itcast.core.controller.pay;

import cn.itcast.core.entity.Result;
import cn.itcast.core.service.pay.PayService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private PayService payService;

    /**
     * 生成支付的二维码
     * @return
     */
    @RequestMapping("/createNative.do")
    public Map<String, String> createNative() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative(username);
    }

    /**
     * 查询交易的订单
     * @param out_trade_no
     * @return
     */
    @RequestMapping("/queryPayStatus.do")
    public Result queryPayStatus(String out_trade_no){
        try{
            int time = 0;
            while (true){
                Map<String, String> map = payService.queryPayStatus(out_trade_no);
                String trade_state = map.get("trade_state");
                if("SUCCESS".equals(trade_state)){
                    // 交易成功
                    return new Result(true, "支付成功");
                } else{
                    // 其他的状态
                    Thread.sleep(5000);
                    time++;
                }

                // 如果超过半个小时，让二维码失效 1800/5 = 360
                if(time > 360){
                    return new Result(false, "二维码超时");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "支付失败");
        }
    }
}
