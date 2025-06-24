package com.pragma.domain.exceptions;

import com.pragma.domain.exceptions.error.Error;
import lombok.NonNull;

public class TransferException extends BankException {

    public TransferException(@NonNull Error error){
        super(error);
    }

}
