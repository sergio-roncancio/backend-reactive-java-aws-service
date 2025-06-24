package com.pragma.r2dbc.config;

import com.pragma.r2dbc.config.dto.PoolProperties;
import com.pragma.r2dbc.config.dto.PropertiesConnection;
import com.pragma.domain.secretmanager.SecretsManagerPort;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class PostgresqlConnectionPool {

    private static final int MAX_AWAIT_TIME_GET_SECRET_DB = 5;

    @Bean
    ConnectionPool createPool(@Value("${aws.secrets.db}") String dbSecretName,
                              SecretsManagerPort secretsManagerPort,
                              PoolProperties poolProperties){

        var propertiesConnection = secretsManagerPort
                .getSecret(dbSecretName, PropertiesConnection.class)
                .timeout(Duration.ofSeconds(MAX_AWAIT_TIME_GET_SECRET_DB))
                .block();

        if(propertiesConnection == null){
            throw new RuntimeException("Error getting database connection properties");
        }

        return buildConnectionPool(propertiesConnection, poolProperties);
    }

    private ConnectionPool buildConnectionPool(PropertiesConnection propertiesConnection, PoolProperties poolProperties) {

        var connection = PostgresqlConnectionConfiguration.builder()
                .host(propertiesConnection.getHost())
                .port(propertiesConnection.getPort())
                .database(propertiesConnection.getDbname())
                .username(propertiesConnection.getUsername())
                .password(propertiesConnection.getPassword())
                .build();

        var pool = ConnectionPoolConfiguration.builder()
                .connectionFactory(new PostgresqlConnectionFactory(connection))
                .name(poolProperties.getName())
                .initialSize(poolProperties.getInitialSize())
                .maxSize(poolProperties.getMaxSize())
                .maxIdleTime(Duration.ofMinutes(poolProperties.getMaxIdleTime()))
                .validationQuery(poolProperties.getValidationQuery())
                .build();

        return new ConnectionPool(pool);
    }

}
