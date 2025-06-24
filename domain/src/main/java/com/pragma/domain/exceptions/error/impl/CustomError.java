package com.pragma.domain.exceptions.error.impl;

import com.pragma.domain.exceptions.error.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomError implements Error {

    private final String message;
    private final String code;

    public CustomError (Error error, Object... argumentMessage){
        this.message = String.format(error.getMessage(), argumentMessage);
        this.code = error.getCode();
    }

}
