package com.pragma.secretmanager.exception;

public class SecretException extends RuntimeException{

    public SecretException(String message){
        super(message);
    }

    public SecretException(String message, Throwable exception){
        super(message, exception);
    }

}
