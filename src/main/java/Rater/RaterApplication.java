package Rater;

import Rater.Security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class RaterApplication {
	public static void main(String[] args) {
		SpringApplication.run(RaterApplication.class, args);
	}
}
