package com.pragma.secretmanager.manager;

import com.pragma.domain.commons.gson.GsonPort;
import com.pragma.secretmanager.exception.SecretException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AWSSecretsManagerTest {

    @Mock
    private SecretsManagerAsyncClient secretsManagerClient;
    @Mock
    private GsonPort gsonPort;

    @InjectMocks
    private AWSSecretsManager awsSecretsManager;

    private static final String SECRET_NAME = "SECRET-NAME";
    private static final String USER = "USER";
    private static final String PASSWORD = "PASSWORD";
    private static final String SECRET_JSON_VALUE = String.format("{\"user\":\"%s\",\"password\":\"%s\"}", USER, PASSWORD);

    @ParameterizedTest
    @MethodSource("provideInvalidSecretName")
    void shouldReturnErrorGetSecret(String secretName){
        var secret = awsSecretsManager.getSecret(secretName);

        StepVerifier.create(secret)
                .expectErrorSatisfies(ex -> {
                    assertInstanceOf(IllegalArgumentException.class, ex);
                }).verify();
    }

    private static Stream<Arguments> provideInvalidSecretName() {
        return Stream.of(null, Arguments.of(""), Arguments.of(" "));
    }

    @Test
    void shouldReturnSecretAsStringGetSecret(){

        var getSecretValueResponse = GetSecretValueResponse.builder().secretString(SECRET_JSON_VALUE).build();

        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class)))
                .thenReturn(CompletableFuture.supplyAsync(() -> getSecretValueResponse));

        var secret = awsSecretsManager.getSecret(SECRET_NAME);

        StepVerifier.create(secret)
                .expectNextMatches(secretValue -> secretValue.equals(SECRET_JSON_VALUE))
                .verifyComplete();
    }

    @Test
    void shouldReturnSecretErrorGetSecret(){

        Supplier<GetSecretValueResponse> getSecretValueResponse = () -> {
            throw ResourceNotFoundException.create("Not Found", new RuntimeException());
        };

        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class)))
                .thenReturn(CompletableFuture.supplyAsync(getSecretValueResponse));

        var secret = awsSecretsManager.getSecret(SECRET_NAME);

        StepVerifier.create(secret)
                .expectErrorSatisfies(ex -> {
                    assertInstanceOf(SecretException.class, ex);
                }).verify();
    }

    @Test
    void shouldReturnErrorClassGetSecret(){
        var secret = awsSecretsManager.getSecret(SECRET_NAME, null);

        StepVerifier.create(secret)
                .expectErrorSatisfies(ex ->{
                    assertInstanceOf(IllegalArgumentException.class, ex);
                })
                .verify();

    }

    @Test
    void shouldReturnSecretAsObjectGetSecret(){

        var getSecretValueResponse = GetSecretValueResponse.builder().secretString(SECRET_JSON_VALUE).build();
        var secretObject = new SecretObject(USER, PASSWORD);

        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class)))
                .thenReturn(CompletableFuture.supplyAsync(() -> getSecretValueResponse));
        when(gsonPort.convertObject(SECRET_JSON_VALUE, SecretObject.class))
                .thenReturn(secretObject);

        var secret = awsSecretsManager.getSecret(SECRET_NAME, SecretObject.class);

        StepVerifier.create(secret)
                .expectNextMatches(secretValue -> {
                    assertEquals(USER, secretValue.user());
                    assertEquals(PASSWORD, secretValue.password());
                    return true;
                })
                .verifyComplete();
    }

}

record SecretObject (String user, String password) {}