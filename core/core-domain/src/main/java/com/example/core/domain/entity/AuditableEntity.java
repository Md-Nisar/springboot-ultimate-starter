package com.example.core.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Audited
@MappedSuperclass
@Getter
@Setter
@ToString
@Access(AccessType.FIELD)
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false, nullable = false, length = 50)
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_ON", updatable = false, nullable = false)
    private OffsetDateTime createdOn;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY", length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_ON")
    private OffsetDateTime lastModifiedOn;

}

