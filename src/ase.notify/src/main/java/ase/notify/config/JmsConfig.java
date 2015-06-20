package ase.notify.config;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 * Created by Michael on 19.06.2015.
 */
//@Configuration
//@EnableJms
public class JmsConfig {

    private static final String JMS_BROKER_URL = "vm://192.168.217.130:61616";

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();

        activeMQConnectionFactory.setBrokerURL(JMS_BROKER_URL);
        activeMQConnectionFactory.setUserName("admin");
        activeMQConnectionFactory.setPassword("admin");

        return activeMQConnectionFactory;
    }
}
