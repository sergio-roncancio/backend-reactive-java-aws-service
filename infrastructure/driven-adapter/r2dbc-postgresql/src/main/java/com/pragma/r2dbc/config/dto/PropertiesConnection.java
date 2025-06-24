package com.pragma.r2dbc.config.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PropertiesConnection {

    private String host;
    private int port;
    private String username;
    private String password;
    private String dbname;

}
