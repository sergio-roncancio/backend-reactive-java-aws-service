package com.pragma.domain.commons.gson;

public interface GsonPort {

    <T> T convertObject(String json, Class<T> cls);

}
