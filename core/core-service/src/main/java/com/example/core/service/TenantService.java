package com.example.core.service;

import com.example.core.base.vo.TenantData;
import com.example.core.base.vo.TenantSearchData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface TenantService {

    TenantData createTenant(TenantData tenant);

    TenantData updateTenant(Integer id, TenantData tenant);

    void deleteTenant(Integer id);

    Optional<TenantData> getTenantById(Integer id);

    List<TenantData> getAllTenants();

    List<TenantData> searchTenants(TenantSearchData searchData);
}
