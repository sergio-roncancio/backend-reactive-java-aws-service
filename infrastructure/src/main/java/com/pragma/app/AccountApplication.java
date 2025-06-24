package com.pragma.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pragma.*"})
public class AccountApplication {

    public static void main(String[] args) {
         SpringApplication.run(AccountApplication.class, args);
    }

}
