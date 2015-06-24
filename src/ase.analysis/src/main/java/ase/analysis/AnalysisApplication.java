package ase.analysis;


import ase.shared.ASEModelMapper;
import ase.shared.commands.CommandFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableHypermediaSupport(type= EnableHypermediaSupport.HypermediaType.HAL)
@ComponentScan(basePackages = { "ase.analysis" }, basePackageClasses = {CommandFactory.class, ASEModelMapper.class})
@EnableJms
public class AnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalysisApplication.class, args);
    }
}
