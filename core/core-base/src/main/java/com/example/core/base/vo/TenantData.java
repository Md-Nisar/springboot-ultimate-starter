package com.example.core.base.vo;

import com.example.core.base.enums.TenantStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tenant details")
public record TenantData(

        @Schema(description = "id", example = "1")
        Integer id,

        @Schema(description = "Name of the tenant", example = "Apple Inc.")
        String name,

        @Schema(description = "Status of the tenant", example = "ACTIVE")
        TenantStatus status,

        @Schema(description = "Indicates if the tenant is deleted", example = "false")
        boolean deleted
) {
    public TenantData(String name, TenantStatus status) {
        this(null, name, status, false);
    }
}

