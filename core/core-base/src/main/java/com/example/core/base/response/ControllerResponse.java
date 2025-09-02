package com.example.core.base.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ControllerResponse<T> {
    private T data;

    private boolean success;

    private int code;

    private String message;

    private Instant timestamp;
}

