package com.allsaints.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SsoAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsoAuthApplication.class, args);
	}

}
