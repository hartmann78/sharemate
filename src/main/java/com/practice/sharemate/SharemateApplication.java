package com.practice.sharemate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.practice.sharemate")
@ComponentScan({"com.practice.sharemate"})
@EntityScan("com.practice.sharemate")
public class SharemateApplication {
    public static void main(String[] args) {
        SpringApplication.run(SharemateApplication.class, args);
    }
}
