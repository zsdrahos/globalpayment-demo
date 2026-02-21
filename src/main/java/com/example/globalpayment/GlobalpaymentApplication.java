package com.example.globalpayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GlobalpaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlobalpaymentApplication.class, args);
	}

}
