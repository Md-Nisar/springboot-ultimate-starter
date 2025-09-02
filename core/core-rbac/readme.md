Got it ✅
Here’s a **`README.md`-ready** RBAC documentation format that includes both explanation and the ASCII tree for your operations.

---

```markdown
# RBAC (Role-Based Access Control) URL Mapping

This document explains how **features** and **operations** are structured in the RBAC system, and how their **URLs** are resolved and inherited.

---

## 📌 Structure

- **Feature** → Top-level domain of functionality (e.g., `ANALYTICS`).
- **Operation** → Specific access level or action for a feature (e.g., `DASHBOARD_ADMIN`, `DASHBOARD_USER`).
- **URL Mapping** → Each operation has one or more API endpoint patterns it can access.
- **Inheritance** → Child operations automatically inherit URLs from their parents.

---

## 🔗 URL Inheritance Rules

1. **Direct URLs**: Defined explicitly in the operation.
2. **Inherited URLs**: Collected from parent operations via `getAllUrls()` method.
3. **Merging**: Uses Java Streams (`Stream.concat`) to merge parent and child URLs, removing duplicates with `.distinct()`.

Example:
- `DASHBOARD_ADMIN` inherits `/v1/analytics/dashboards/**` from `DASHBOARD_USER`.

---

## 🌳 Operations URL Tree

```

ANALYTICS
│
├── DASHBOARD
│   │
│   ├── DASHBOARD\_USER
│   │   ├── /v1/analytics/dashboards/\*\*
│   │
│   └── DASHBOARD\_ADMIN
│       ├── /v1/admin/analytics/dashboards/\*\*
│       ├── (Inherited) /v1/analytics/dashboards/\*\*
│
└── DATASOURCE
│
├── DATASOURCE\_USER
│   ├── /v1/analytics/datasource/\*\*
│
└── DATASOURCE\_ADMIN
├── /v1/analytics/datasource/admin/\*\*
├── (Inherited) /v1/analytics/datasource/\*\*

````

---

## 🛠 Example `getAllUrls()` Implementation

```java
public List<String> getAllUrls() {
    return Stream.concat(
            urls.stream(),
            children.stream()
                    .flatMap(c -> c.getAllUrls().stream())
    )
    .distinct()
    .toList();
}
````

---

## 📋 Benefits of This Structure

* **Scalable** → Easily add new operations without breaking existing mappings.
* **DRY Principle** → URLs defined once in the parent and reused via inheritance.
* **Clarity** → ASCII tree gives a quick overview of all operations and their URLs.
* **Flexible** → Works for multiple hierarchies like `Feature -> Admin, User` or deeper nesting.

---

```

---

If you want, I can **extend this README** to also include a table format listing:  
- **Operation Name**
- **Direct URLs**
- **Inherited URLs**  
so that your devs can quickly search and see access permissions without reading the tree. That would make it both **human-readable** and **machine-verifiable**.  

Do you want me to add that table version too?
```
