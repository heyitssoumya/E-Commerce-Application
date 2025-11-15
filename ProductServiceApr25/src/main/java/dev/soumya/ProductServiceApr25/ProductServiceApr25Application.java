package dev.soumya.ProductServiceApr25;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProductServiceApr25Application {

	public static void main(String[] args) {

		SpringApplication.run(ProductServiceApr25Application.class, args);
	}

}
