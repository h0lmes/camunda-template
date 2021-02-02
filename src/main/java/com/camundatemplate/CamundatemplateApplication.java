package com.camundatemplate;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class CamundatemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundatemplateApplication.class, args);
	}
}
