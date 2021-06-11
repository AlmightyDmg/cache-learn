// CHECKSTYLE:OFF
package com.haizhi.databridge;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@RetrofitScan(basePackages = "com.haizhi.databridge.client")
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class DatabridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabridgeApplication.class, args);
    }
}
