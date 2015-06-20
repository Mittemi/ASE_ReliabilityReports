package ase.analysis;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
@EnableScheduling
public class AnalysisApplication {

    private static final String JMS_BROKER_URL = "tcp://192.168.217.130:61616";

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory());
        return cachingConnectionFactory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(JMS_BROKER_URL);
        activeMQConnectionFactory.setUserName("bot");
        activeMQConnectionFactory.setPassword("blahblah");

        return activeMQConnectionFactory;
    }
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        return jmsTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(AnalysisApplication.class, args);
    }
}
