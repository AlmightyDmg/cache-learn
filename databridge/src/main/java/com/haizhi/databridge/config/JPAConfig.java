package com.haizhi.databridge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class JPAConfig {
	
	@Bean
	public IsDeleteAspect deleteAspect() {
		return new IsDeleteAspect();
	}
}
