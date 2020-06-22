package ru.nomia.test.task.nomia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class NomiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NomiaApplication.class, args);
	}

}
