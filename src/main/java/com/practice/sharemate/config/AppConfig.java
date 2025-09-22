package com.practice.sharemate.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.practice.sharemate")
@ComponentScan({"com.practice.sharemate"})
@EntityScan("com.practice.sharemate")
public class AppConfig {
}
