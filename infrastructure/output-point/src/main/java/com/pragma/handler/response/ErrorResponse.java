package com.pragma.handler.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    @Setter
    private List<String> errorDetails;
    private final LocalDateTime timestamp;

}
