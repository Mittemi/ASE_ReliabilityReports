package ase.notify;

import ase.shared.ASEModelMapper;
import ase.shared.commands.CommandFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotifyApplication.class)
@ComponentScan(basePackages = { "ase.notify" }, basePackageClasses = {CommandFactory.class, ASEModelMapper.class})
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
