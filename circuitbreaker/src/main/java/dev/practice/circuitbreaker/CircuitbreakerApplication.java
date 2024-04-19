package dev.practice.circuitbreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CircuitbreakerApplication {

	/**
	 * https://resilience4j.readme.io/docs/circuitbreaker
	 */
	public static void main(String[] args) {
		SpringApplication.run(CircuitbreakerApplication.class, args);
	}

}
