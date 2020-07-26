package com.sulikdan.ERDMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class ErdmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErdmsApplication.class, args);
	}

	@Bean("threadPoolTaskExecutor")
	public TaskExecutor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(1);
		executor.setQueueCapacity(250);
		executor.setThreadNamePrefix("AsyncApiRequests-");
		executor.initialize();
		return executor;
	}

	@Bean("docsScanningTaskExecutor")
	public TaskExecutor docsScanningExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(1);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("docsScanning-");
		executor.initialize();
		return executor;
	}
}
