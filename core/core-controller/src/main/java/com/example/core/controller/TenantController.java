package com.example.core.controller;

import com.example.core.base.enums.ResponseMessage;
import com.example.core.base.response.ControllerResponse;
import com.example.core.base.vo.TenantData;
import com.example.core.base.vo.TenantSearchData;
import com.example.core.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/v1/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenants", description = "Tenant management operations")
public class TenantController extends BaseController {

    private final TenantService tenantService;

    @PostMapping
    @Operation(
            summary = "Create a new tenant",
            description = "Creates and persists a new tenant record in the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tenant created successfully",
                            content = @Content(schema = @Schema(implementation = TenantData.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload or validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have necessary permissions")
            }
    )
    public ResponseEntity<ControllerResponse<TenantData>> createTenant(@RequestBody TenantData tenantData) {
        TenantData tenant = tenantService.createTenant(tenantData);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tenant.id())
                .toUri();
        return created(location, tenant, ResponseMessage.TENANT_CREATED_SUCCESSFULLY.getValue());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing tenant",
            description = "Updates the details of a specific tenant identified by its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tenant updated successfully",
                            content = @Content(schema = @Schema(implementation = TenantData.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload or validation error"),
                    @ApiResponse(responseCode = "404", description = "Tenant not found for the given ID"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have necessary permissions")
            }
    )
    public ResponseEntity<ControllerResponse<TenantData>> updateTenant(@PathVariable Integer id, @RequestBody TenantData tenantData) {
        TenantData tenant = tenantService.updateTenant(id, tenantData);
        return ok(tenant, ResponseMessage.TENANT_UPDATED_SUCCESSFULLY.getValue());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a tenant by ID",
            description = "Permanently deletes a tenant record from the database.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tenant deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Tenant not found for the given ID"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have necessary permissions")
            }
    )
    public ResponseEntity<ControllerResponse<Void>> delete(@PathVariable Integer id) {
        tenantService.deleteTenant(id);
        return noContent(ResponseMessage.TENANT_DELETED_SUCCESSFULLY.getValue());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve a tenant by ID",
            description = "Fetches the details of a single tenant using its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tenant details fetched successfully",
                            content = @Content(schema = @Schema(implementation = TenantData.class))),
                    @ApiResponse(responseCode = "404", description = "Tenant not found for the given ID"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
            }
    )
    public ResponseEntity<ControllerResponse<TenantData>> getTenantById(@PathVariable Integer id) {
        Optional<TenantData> tenant = tenantService.getTenantById(id);
        return tenant.map(t -> ok(t, ResponseMessage.TENANT_FETCHED_SUCCESSFULLY.getValue()))
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.TENANT_NOT_FOUND.getValue().formatted(id)));
    }

    @GetMapping
    @Operation(
            summary = "Get all tenants",
            description = "Retrieves a list of all tenants registered in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of tenants fetched successfully",
                            content = @Content(schema = @Schema(implementation = TenantData.class)))
            }
    )
    public ResponseEntity<ControllerResponse<List<TenantData>>> getAllTenants() {
        List<TenantData> allTenants = tenantService.getAllTenants();
        return ok(allTenants, ResponseMessage.ALL_TENANTS_FETCHED_SUCCESSFULLY.getValue());
    }

    @PostMapping("/search")
    @Operation(
            summary = "Search for tenants",
            description = "Performs a search for tenants based on provided filter parameters like name or status.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of tenants matching the search criteria",
                            content = @Content(schema = @Schema(implementation = TenantData.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid search parameters")
            }
    )
    public ResponseEntity<ControllerResponse<List<TenantData>>> searchTenants(@RequestBody @Valid TenantSearchData searchData) {
        List<TenantData> tenants = tenantService.searchTenants(searchData);
        return ok(tenants, ResponseMessage.TENANTS_FETCHED_SUCCESSFULLY.getValue());
    }

}
