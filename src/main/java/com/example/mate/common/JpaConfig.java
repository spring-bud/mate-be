package com.example.mate.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = {
                "com.example.mate.common.outbox",
                "com.example.mate.*.domain.repository",
        }
)
public class JpaConfig {
}
