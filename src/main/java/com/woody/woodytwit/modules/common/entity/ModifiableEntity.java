package com.woody.woodytwit.modules.common.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public class ModifiableEntity {

    @CreationTimestamp
    @Column(name = "create_time",
            updatable = false,
            nullable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "modified_time",
            nullable = false)
    private LocalDateTime modifiedTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }
}
