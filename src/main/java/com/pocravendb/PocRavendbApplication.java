package com.pocravendb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.pocravendb.config")
@SpringBootApplication
public class PocRavendbApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocRavendbApplication.class, args);
	}
}
