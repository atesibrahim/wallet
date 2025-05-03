package com.ing.wallet.authentication.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "create_by", nullable = false, length = 200, updatable = false)
    @JsonIgnore
    private String createBy;

    @CreatedDate
    @Column(name = "create_date", nullable = false, updatable = false)
    @JsonIgnore
    private Date createDate;

    @LastModifiedBy
    @Column(name = "update_by", length = 200)
    @JsonIgnore
    private String updateBy;

    @LastModifiedDate
    @Column(name = "update_date")
    @JsonIgnore
    private Date updateDate;

    @PrePersist
    void prePersist() {
        if (null == createDate) {
            createDate = new Date();
        }
    }

    @PreUpdate
    void preUpdate() {
        updateDate = new Date();
    }

}