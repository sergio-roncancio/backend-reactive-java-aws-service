package com.pragma.domain.exceptions.error.impl;

import com.pragma.domain.exceptions.error.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorDefinition implements Error {

    INTERNAL_SERVER_ERROR("Server internal error", "ER500-1"),

    NOT_FOUNDS_ACCOUNT("Does not have funds to make the operation", "ER400-1"),
    ACCOUNT_NOT_EXIST("Account %s not exist", "ER400-2"),
    ACCOUNT_IS_INACTIVE("Account %s is inactive", "ER400-3"),
    BASIC_VALIDATIONS("Basic validations violated", "ER400-4"),
    ACCOUNT_CAN_NOT_EQUALS("Accounts cannot be equals", "ER400-5"),
    SOME_ACCOUNT_NUMBER_IS_NULL_OR_BLANK("Accounts numbers can not empty or null", "ER400-6");;

    private final String message;
    private final String code;

}
