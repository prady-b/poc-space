package com.prady.sample.tx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringTxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTxDemoApplication.class, args);
    }

}
