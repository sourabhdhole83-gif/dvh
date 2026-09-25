package com.dvh.common;

import lombok.*;

import java.time.LocalDateTime;


@Data@NoArgsConstructor@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timeStamp;
    private String errorCode;
    private String message;
    private String path;
}
