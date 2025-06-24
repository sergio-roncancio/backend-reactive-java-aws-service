package com.pragma.domain.exceptions;

import com.pragma.domain.exceptions.error.Error;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class BankException extends RuntimeException {

    private final Error error;

    public BankException(@NonNull Error error){
        super(error.getMessage());
        this.error = error;
    }

}
