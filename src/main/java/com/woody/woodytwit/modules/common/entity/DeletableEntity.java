package com.woody.woodytwit.modules.common.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class DeletableEntity extends ModifiableEntity {

    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

    protected void softDelete() {
        checkDeletable();
        this.deleteTime = LocalDateTime.now();
    }

    private void checkDeletable() {
        if (deleteTime != null) {
            throw new NotDeletableException();
        }
    }

    @Override
    public LocalDateTime getCreateTime() {
        return super.getCreateTime();
    }

    @Override
    public LocalDateTime getModifiedTime() {
        return super.getModifiedTime();
    }

    public LocalDateTime getDeleteTime() {
        return deleteTime;
    }
}
