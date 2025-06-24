package com.pragma.api.validation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ValidationResquest {

    private final Validator validator;

    public <T> Mono<T> validate(T request){
        return Mono.fromSupplier(() -> validator.validate(request))
                .flatMap(constraintViolations -> {
                    if(constraintViolations == null || constraintViolations.isEmpty()){
                        return Mono.just(request);
                    }
                    return Mono.error(new ConstraintViolationException(constraintViolations));
                });
    }

}
