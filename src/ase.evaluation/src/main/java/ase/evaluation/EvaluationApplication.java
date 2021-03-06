package ase.evaluation;

import ase.shared.ASEModelMapper;
import ase.shared.commands.CommandFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "ase.evaluation" }, basePackageClasses = {CommandFactory.class, ASEModelMapper.class})
public class EvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvaluationApplication.class, args);
    }
}
