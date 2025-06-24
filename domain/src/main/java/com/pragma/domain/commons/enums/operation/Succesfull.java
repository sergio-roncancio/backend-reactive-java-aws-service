package com.pragma.domain.commons.enums.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Succesfull {

    OK_TRANSACTION("Transaction completed correctly", "SU201-1"),

    OK_MOVEMENTS_LIST("Movements listed correctly", "SU200-1");

    private final String message;
    private final String code;

}
