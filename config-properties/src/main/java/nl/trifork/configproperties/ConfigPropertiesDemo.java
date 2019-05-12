package nl.trifork.configproperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LootProperties.class)
public class ConfigPropertiesDemo implements CommandLineRunner {

	@Autowired LootProperties loot;

	public static void main(String[] args) {
		SpringApplication.run(ConfigPropertiesDemo.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Loot game: " + loot.getGame());
		System.out.println("Loot transfer allowed: " + loot.getAllowTransfer());
	}
}
