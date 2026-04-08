package com.tadiwanashe.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WmsOrderOrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(WmsOrderOrchestratorApplication.class, args);
	}
}