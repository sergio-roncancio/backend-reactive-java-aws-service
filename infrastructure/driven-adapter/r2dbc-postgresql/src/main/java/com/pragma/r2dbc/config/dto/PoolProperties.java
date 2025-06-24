package com.pragma.r2dbc.config.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "pool.r2dbc")
public class PoolProperties {

    private String name;
    private int initialSize;
    private int maxSize;
    private int maxIdleTime;
    private String validationQuery;

}
