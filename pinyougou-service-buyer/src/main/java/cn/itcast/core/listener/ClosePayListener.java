package cn.itcast.core.listener;

import cn.itcast.core.service.pay.PayService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ClosePayListener implements MessageListener {

    @Resource
    private PayService payService;

    @Override
    public void onMessage(Message message) {
        try {
            // 取出消息
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
            String orderId = activeMQTextMessage.getText();
            System.out.println("订单id："+orderId);
            // 消费消息
            try {
                payService.closePay(orderId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
