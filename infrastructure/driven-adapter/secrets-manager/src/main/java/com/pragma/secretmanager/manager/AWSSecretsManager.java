package com.pragma.secretmanager.manager;

import com.pragma.domain.secretmanager.SecretsManagerPort;
import com.pragma.secretmanager.exception.SecretException;
import com.pragma.domain.commons.gson.GsonPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class AWSSecretsManager implements SecretsManagerPort {

    private final SecretsManagerAsyncClient secretsManagerClient;
    private final GsonPort gsonPort;

    @Override
    public Mono<String> getSecret(String secretName) {
        return getSecretValue(secretName);
    }

    @Override
    public <T> Mono<T> getSecret(String secretName, Class<T> cls) {
        if(cls == null)
            return Mono.empty();
        return getSecretValue(secretName)
                .map(secret -> gsonPort.convertObject(secret, cls));
    }

    private Mono<String> getSecretValue(String secretName){
        if(isBlank(secretName)){
            return Mono.error(() -> new SecretException("Secret name must not be blank or null"));
        }
        var requestSecret = GetSecretValueRequest.builder().secretId(secretName).build();
        return Mono.fromFuture(() -> secretsManagerClient.getSecretValue(requestSecret))
                .onErrorMap(e -> new SecretException("Error getting secret with name " + secretName, e))
                .map(GetSecretValueResponse::secretString);
    }

}
