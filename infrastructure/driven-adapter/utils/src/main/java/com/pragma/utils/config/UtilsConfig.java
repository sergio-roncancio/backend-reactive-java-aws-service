package com.pragma.utils.config;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class UtilsConfig {

    @Bean
    Gson gson(){
        return new Gson();
    }

    @Bean
    ModelMapper modelMapper(){
        var modelMapper = new ModelMapper();
        modelMapper.registerModule(new RecordModule());
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        return modelMapper;
    }

}
