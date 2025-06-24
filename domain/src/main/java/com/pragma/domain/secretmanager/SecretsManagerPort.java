package com.pragma.domain.secretmanager;

import reactor.core.publisher.Mono;

public interface SecretsManagerPort {

    Mono<String> getSecret(String secretName);
    <T> Mono<T> getSecret(String secretName, Class<T> cls);

}
