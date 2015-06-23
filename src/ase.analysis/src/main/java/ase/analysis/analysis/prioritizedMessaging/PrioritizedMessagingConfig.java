package ase.analysis.analysis.prioritizedMessaging;

import ase.analysis.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

/**
 * Created by Michael on 21.06.2015.
 */
@Configuration
public class PrioritizedMessagingConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public PrioritizedJmsTemplate prioritizedJmsTemplate() {

        PrioritizedJmsTemplate jmsTemplate = new PrioritizedJmsTemplate(connectionFactory);
        jmsTemplate.setDefaultDestinationName(Constants.ANALYSIS_QUEUE_NAME);
        return jmsTemplate;
    }
}
