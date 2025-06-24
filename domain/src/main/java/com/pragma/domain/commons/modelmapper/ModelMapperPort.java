package com.pragma.domain.commons.modelmapper;

import reactor.core.publisher.Mono;

public interface ModelMapperPort {

    <T, R> Mono<R> mapReactive(T object, Class<R> cls);
    <T, R> R map(T object, Class<R> cls);

}
