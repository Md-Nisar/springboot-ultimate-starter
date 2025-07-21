package com.example.core.domain.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Audited
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = false)
public class TenantAuditableEntity extends AuditableEntity {

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "TNT_PK_ID")
    private Tenant tenant;
}
