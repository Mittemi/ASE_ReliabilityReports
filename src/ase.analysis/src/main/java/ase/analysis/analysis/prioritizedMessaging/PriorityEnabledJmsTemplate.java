package ase.analysis.analysis.prioritizedMessaging;

import org.springframework.jms.core.JmsTemplate;

/**
 * Created by Michael on 21.06.2015.
 */
public class PriorityEnabledJmsTemplate extends JmsTemplate {

    public<T> void sendMessage(String destination, T content, int priorityLevel ) {

    }
}
