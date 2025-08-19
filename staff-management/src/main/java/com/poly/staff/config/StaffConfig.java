package com.poly.staff.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.poly.staff.repository")
@EntityScan(basePackages = "com.poly.staff.entity")
public class StaffConfig {
    // Configuration for JPA repositories and entity scanning
}
