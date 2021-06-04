// CHECKSTYLE:OFF
package com.haizhi.databridge;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.haizhi.data.jpa.HaizhiStandardRepositoryImpl;


@EnableJpaRepositories(repositoryBaseClass = HaizhiStandardRepositoryImpl.class)
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
@RetrofitScan(basePackages = "com.haizhi.databridge.client")
public class DatabridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabridgeApplication.class, args);
    }
}
