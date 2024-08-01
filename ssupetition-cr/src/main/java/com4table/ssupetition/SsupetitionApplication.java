package com4table.ssupetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
public class SsupetitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsupetitionApplication.class, args);
	}

}
