package com.example.core.domain.dao;

import com.example.core.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantDao extends JpaRepository<Tenant, Integer> {
}
