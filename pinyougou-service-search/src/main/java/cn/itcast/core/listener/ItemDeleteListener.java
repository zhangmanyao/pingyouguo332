package cn.itcast.core.listener;

import cn.itcast.core.service.search.ItemSearchService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 自定义消息监听器：删除索引库中的数据
 */
public class ItemDeleteListener implements MessageListener {

    @Resource
    private ItemSearchService itemSearchService;

    /**
     * 获取容器中的消息并且进行消费
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            // 取出消息
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
            String id = activeMQTextMessage.getText();
            System.out.println("消费者search获取到的id："+id);
            // 消费消息
            itemSearchService.deleteItemFromSolr(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
