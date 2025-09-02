package com.example.core.rbac.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Enum representing the hierarchical structure of RBAC operations.
 */
@Getter
public enum Operation {

    DASHBOARD_USER("analytics.dashboard.user",
            List.of("/v1/analytics/dashboards/**"),
            List.of()),

    DASHBOARD_ADMIN("analytics.dashboard.admin",
            List.of("/v1/admin/analytics/dashboards/**"),
            List.of(DASHBOARD_USER)),

    DASHBOARD("analytics.dashboard",
            List.of(),
            List.of(DASHBOARD_USER, DASHBOARD_ADMIN)),

    DATASOURCE_USER("analytics.datasource.user",
            List.of("/v1/analytics/datasource/**"),
            List.of()),

    DATASOURCE_ADMIN("analytics.datasource.admin",
            List.of("/v1/admin/analytics/datasource/**"),
            List.of(DATASOURCE_USER)),

    DATASOURCE("analytics.datasource",
            List.of(),
            List.of(DATASOURCE_USER, DATASOURCE_ADMIN)),

    ANALYTICS("analytics",
            List.of(),
            List.of(DASHBOARD, DATASOURCE));

    private final String key;
    private final List<String> urls;
    private final List<Operation> children;

    Operation(String key, List<String> urls, List<Operation> children) {
        this.key = key;
        this.urls = urls;
        this.children = children;
    }

    public boolean isComposite() {
        return !children.isEmpty();
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean matchesUrl(String requestUrl) {
        return urls.stream().anyMatch(requestUrl::startsWith);
    }

    public boolean matchesUrlRecursive(String requestUrl) {
        return matchesUrl(requestUrl) ||
                children.stream().anyMatch(c -> c.matchesUrlRecursive(requestUrl));
    }

    public List<String> getAllUrls() {
        return Stream.of(
                        urls.stream(),
                        children.stream().flatMap(c -> c.getAllUrls().stream())
                )
                .flatMap(s -> s)
                .distinct()
                .toList();
    }

    public Map<String, Object> getUrlTree() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key", key);
        map.put("urls", urls);
        map.put("children", children.stream()
                .map(Operation::getUrlTree)
                .toList());
        return map;
    }

    public static List<Operation> findBySuffix(String suffix) {
        return Stream.of(values())
                .filter(op -> op.getKey().endsWith(suffix))
                .toList();
    }

    public static List<Operation> adminOperations() {
        return findBySuffix(".admin");
    }

    public static List<Operation> userOperations() {
        return findBySuffix(".user");
    }

    public static List<Operation> modules() {
        return List.of(ANALYTICS);
    }

    public static List<Operation> features(Operation module) {
        return module.getChildren();
    }

    public static List<Operation> operations(Operation feature) {
        return feature.getChildren();
    }

}

