package com.pragma.secretmanager.config;


import com.pragma.secretmanager.config.dto.SecretManagerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;

import java.net.URI;

@Configuration
public class AwsSecretManagerClient {

    @Bean
    SecretsManagerAsyncClient awsCredentialsProvider(SecretManagerProperties properties){
        return SecretsManagerAsyncClient.builder().credentialsProvider(
                DefaultCredentialsProvider.builder().build())
                .region(Region.of(properties.getRegion()))
                .endpointOverride(URI.create(properties.getEndpoint()))
                .build();
    }

}
