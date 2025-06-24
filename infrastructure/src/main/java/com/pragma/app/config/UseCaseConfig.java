package com.pragma.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.springframework.context.annotation.FilterType.REGEX;

@Configuration
@ComponentScan(
        basePackages = "com.pragma.usecase.*",
        includeFilters = @ComponentScan.Filter(type = REGEX, pattern = ".*UseCase$")
)
public class UseCaseConfig {
}
