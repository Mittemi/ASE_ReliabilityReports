package ase.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.util.FileSystemUtils;

import java.io.File;

@SpringBootApplication
@EnableJms
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) {
        // Clean out any ActiveMQ data from a previous run
        FileSystemUtils.deleteRecursively(new File("activemq-data"));

        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);



    }
}
