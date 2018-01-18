package com.csy.GrabaTicket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.csy.GrabaTicket.*")
public class GrabaTicketApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Object[] {GrabaTicketApplication.class,GrabaTicketConfig.class}, args);
	}
}
