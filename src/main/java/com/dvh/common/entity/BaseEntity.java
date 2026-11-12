package com.dvh.common.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.*;


@MappedSuperclass //“ye class khud table nahi banegi, par iske fields jo bhi child entity isse extend kare, uski table mein columns ban jayenge.”
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at",nullable = false,updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at",nullable = false)
    private Instant updatedAt;
}
