package com.example.core.base.vo;

import com.example.core.base.enums.TenantStatus;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

/**
 * Immutable search/filter DTO for Tenant.
 */
@Value
@Builder
public class TenantSearchData implements ValueObject {
    String name;
    TenantStatus status;
    Boolean deleted;

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<TenantStatus> getStatus() {
        return Optional.ofNullable(status);
    }

    public Optional<Boolean> getDeleted() {
        return Optional.ofNullable(deleted);
    }
}
