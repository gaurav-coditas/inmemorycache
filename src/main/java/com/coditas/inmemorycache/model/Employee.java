package com.coditas.inmemorycache.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee extends BaseEntity{

    /** name. */
    private String name;

    /**
     *
     * @return emp name
     */
    @Column
    public String getName() {
        return name;
    }

    /**
     *
     * @param name emp name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
