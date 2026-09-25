package com.dvh.common;

import lombok.*;

@Data@AllArgsConstructor@NoArgsConstructor
public class APIResponse <T>{
    private boolean success;
    private String message;
    private T data;
}
