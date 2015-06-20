package ase.datasource;

import ase.datasource.simulation.DataSimulation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DataSourceApplication {

    @Bean
    public DataSimulation dataSimulation() {
        return new DataSimulation();
    }

    public static void main(String[] args) {
        SpringApplication.run(DataSourceApplication.class, args);
    }
}
