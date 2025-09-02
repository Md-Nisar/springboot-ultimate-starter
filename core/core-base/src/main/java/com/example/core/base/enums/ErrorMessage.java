package com.example.core.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    TENANT_CREATION_FAILED("Tenant creation failed");

    private final String value;

}
