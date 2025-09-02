package com.example.core.domain.dao;

import com.example.core.base.enums.TenantStatus;
import com.example.core.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TenantDao extends JpaRepository<Tenant, Integer> {

    Optional<Tenant> findByName(String name);

    List<Tenant> findByStatus(TenantStatus status);

    List<Tenant> findByDeleted(boolean deleted);

    List<Tenant> findByStatusAndDeleted(TenantStatus status, boolean deleted);
}
