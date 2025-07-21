package com.example.core.domain.entity;


import com.example.core.base.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Audited
@Entity
@Table(name = "CORE_TENANT", indexes = {
        @Index(name = "IDX_TENANT_STATUS", columnList = "STATUS"),
})
@Data
@EqualsAndHashCode(callSuper = false)
@Access(AccessType.FIELD)
@EntityListeners(AuditingEntityListener.class)
public class Tenant extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TNT_PK_ID")
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private TenantStatus status = TenantStatus.ACTIVE;

    @Column(name = "TIME_ZONE")
    private String timeZone;

    @Column(name = "CONTACT_EMAIL")
    private String contactEmail;

}

