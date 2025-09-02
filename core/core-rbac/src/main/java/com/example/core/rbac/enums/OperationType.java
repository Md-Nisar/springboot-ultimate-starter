package com.example.core.rbac.enums;

public enum OperationType {
    MODULE,    // High-level grouping, may contain features
    FEATURE,   // Mid-level grouping, may contain operations
    PERMISSION     // Lowest level, actual permission
}

