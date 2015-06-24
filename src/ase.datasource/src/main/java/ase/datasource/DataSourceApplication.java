package ase.datasource;

import ase.datasource.simulation.DataSimulation;
import ase.shared.ASEModelMapper;
import ase.shared.commands.CommandFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "ase.datasource" }, basePackageClasses = {CommandFactory.class, ASEModelMapper.class})
public class DataSourceApplication {

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Bean
    public DataSimulation dataSimulation() {

        DataSimulation dataSimulation = new DataSimulation();
        autowireCapableBeanFactory.autowireBean(dataSimulation);
        return dataSimulation;
    }

    public static void main(String[] args) {
        SpringApplication.run(DataSourceApplication.class, args);
    }
}
