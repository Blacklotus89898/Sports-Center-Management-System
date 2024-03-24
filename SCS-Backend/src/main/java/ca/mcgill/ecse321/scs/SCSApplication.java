package ca.mcgill.ecse321.scs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "SCS API Documentation", version = "1.0", description = "SCS API Documentation for ECSE321 Group Project 2024."))
public class SCSApplication {
	public static void main(String[] args) {
		SpringApplication.run(SCSApplication.class, args);
	}

}
