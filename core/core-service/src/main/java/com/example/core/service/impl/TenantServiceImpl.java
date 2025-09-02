package com.example.core.service.impl;

import com.example.core.base.vo.TenantData;
import com.example.core.base.vo.TenantSearchData;
import com.example.core.service.TenantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantServiceImpl implements TenantService {

    @Override
    public TenantData createTenant(TenantData tenant) {
        return null;
    }

    @Override
    public TenantData updateTenant(Integer id, TenantData tenant) {
        return null;
    }

    @Override
    public void deleteTenant(Integer id) {

    }

    @Override
    public Optional<TenantData> getTenantById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<TenantData> getAllTenants() {
        return List.of();
    }

    @Override
    public List<TenantData> searchTenants(TenantSearchData searchData) {
        return List.of();
    }
}
