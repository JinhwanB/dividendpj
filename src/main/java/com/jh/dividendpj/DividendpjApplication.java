package com.jh.dividendpj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DividendpjApplication {

    public static void main(String[] args) {
        SpringApplication.run(DividendpjApplication.class, args);
    }

}
