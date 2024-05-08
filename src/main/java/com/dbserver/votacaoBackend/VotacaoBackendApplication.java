package com.dbserver.votacaoBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class VotacaoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotacaoBackendApplication.class, args);
	}

}
