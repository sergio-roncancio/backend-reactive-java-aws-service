package com.pragma.domain.exceptions;

import com.pragma.domain.exceptions.error.Error;
import lombok.NonNull;

public class AccountException extends BankException {

    public AccountException(@NonNull Error error) {
        super(error);
    }

}
