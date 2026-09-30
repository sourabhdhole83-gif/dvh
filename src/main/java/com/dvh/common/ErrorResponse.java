package com.dvh.common;

import lombok.*;

import java.time.LocalDateTime;
import java.util.*;


@Data@NoArgsConstructor@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String errorCode;
    private String message;
    private String path;
    private Map<String, String> details;
}
