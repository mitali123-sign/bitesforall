package openminder.emeal;

import openminder.emeal.config.file.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class EmealApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmealApplication.class, args);
	}

}
