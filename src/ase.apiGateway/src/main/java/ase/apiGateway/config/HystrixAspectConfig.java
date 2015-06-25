package ase.apiGateway.config;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by Michael on 23.06.2015.
 */
@Configuration
//enable hystrix only if running with profile productive
@Profile(value = {"productive"})
public class HystrixAspectConfig {

    @Bean
    public HystrixCommandAspect hystrixAspect() {
        System.out.println("Enable Hystrix Aspect");
        return new HystrixCommandAspect();
    }
}
