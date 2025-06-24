package com.pragma.api.response;

import com.pragma.domain.commons.enums.operation.Succesfull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ResponseFactory {

    public <T> Response<T> createResponse(T data, Succesfull succesfull){
        return Response.<T>builder()
                .code(succesfull.getCode())
                .message(succesfull.getMessage())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }


}
