package com.pragma.api.response;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class Response<T> {

    private String code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

}
