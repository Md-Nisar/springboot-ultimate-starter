package com.example.core.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {

    TENANT_CREATED_SUCCESSFULLY("Tenant created successfully"),
    TENANT_UPDATED_SUCCESSFULLY("Tenant updated successfully"),
    TENANT_DELETED_SUCCESSFULLY("Tenant deleted successfully"),
    TENANT_FETCHED_SUCCESSFULLY("Tenant fetched successfully"),
    TENANTS_FETCHED_SUCCESSFULLY("Tenants fetched successfully"),
    ALL_TENANTS_FETCHED_SUCCESSFULLY("All tenants fetched successfully"),
    TENANT_NOT_FOUND("Tenant not found with id %s");

    private final String value;

}
