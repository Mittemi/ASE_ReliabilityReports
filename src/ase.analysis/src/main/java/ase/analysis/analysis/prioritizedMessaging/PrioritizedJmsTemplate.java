package ase.analysis.analysis.prioritizedMessaging;

import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

/**
 * Created by Michael on 21.06.2015.
 */
public class PrioritizedJmsTemplate extends JmsTemplate {

    public PrioritizedJmsTemplate(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    protected void doSend(MessageProducer producer, Message message) throws JMSException {
        producer.send(message, getDeliveryMode(), message.getJMSPriority(), getTimeToLive());
    }
}
