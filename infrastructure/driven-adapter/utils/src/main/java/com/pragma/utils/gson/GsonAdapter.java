package com.pragma.utils.gson;

import com.google.gson.Gson;
import com.pragma.domain.commons.gson.GsonPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GsonAdapter implements GsonPort {

    private final Gson gson;

    @Override
    public <T> T convertObject(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }
}
