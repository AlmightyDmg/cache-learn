package com.haizhi.databridge;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RetrofitScan(basePackages = "com.haizhi.databridge.client")
@SpringBootApplication
public class DatabridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabridgeApplication.class, args);
    }

}
