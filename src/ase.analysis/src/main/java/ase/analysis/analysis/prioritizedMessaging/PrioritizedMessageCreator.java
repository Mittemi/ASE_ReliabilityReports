package ase.analysis.analysis.prioritizedMessaging;

import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.print.attribute.standard.Destination;

/**
 * Created by Michael on 21.06.2015.
 */
public class PrioritizedMessageCreator implements org.springframework.jms.core.MessageCreator {

    private static SimpleMessageConverter converter = new SimpleMessageConverter();

    private PrioritizedMessage<?> prioritizedMessage;

    public PrioritizedMessageCreator(PrioritizedMessage<?> prioritizedMessage) {
        this.prioritizedMessage = prioritizedMessage;
    }


    @Override
    public Message createMessage(Session session) throws JMSException {

        final javax.jms.Message jmsMsg = converter.toMessage(prioritizedMessage.getContent(), session);
        jmsMsg.setJMSPriority(prioritizedMessage.getMessagePriority().getValue());
        return jmsMsg;
    }
}
