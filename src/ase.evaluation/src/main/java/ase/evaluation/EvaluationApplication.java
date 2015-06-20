package ase.evaluation;

import ase.shared.commands.CommandFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EvaluationApplication {

    @Bean
    public CommandFactory commandFactory() {
        return new CommandFactory();
    }

    public static void main(String[] args) {
        SpringApplication.run(EvaluationApplication.class, args);
    }
}
