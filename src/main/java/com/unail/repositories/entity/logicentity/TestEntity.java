package com.unail.repositories.entity.logicentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by 橘 on 2016/7/2.
 */
public class TestEntity {
    @Id
    @Column(name="id")
    private Long id;
    @Column(name="f1")
    private String field1;
    @Column(name="f2")
    private String field2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}
