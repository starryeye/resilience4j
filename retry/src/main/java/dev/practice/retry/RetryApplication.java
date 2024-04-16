package dev.practice.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RetryApplication {

	/**
	 * https://resilience4j.readme.io/docs/retry
	 */

	public static void main(String[] args) {
		SpringApplication.run(RetryApplication.class, args);
	}

}
