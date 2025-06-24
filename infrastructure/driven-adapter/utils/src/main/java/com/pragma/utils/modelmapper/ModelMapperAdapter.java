package com.pragma.utils.modelmapper;

import com.pragma.domain.commons.modelmapper.ModelMapperPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModelMapperAdapter implements ModelMapperPort {

    private final ModelMapper modelMapper;

    @Override
    public <T, R> Mono<R> mapReactive(T object, Class<R> cls) {
        return Mono.fromSupplier(() -> modelMapper.map(object, cls))
                .onErrorResume(e -> {
                    log.error("Error mapping class {}", cls, e);
                    return Mono.error(e);
                });
    }

    @Override
    public <T, R> R map(T object, Class<R> cls) {
        return modelMapper.map(object, cls);
    }

}
