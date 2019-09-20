package com.coditas.inmemorycache.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee {

    /** id. */
    private Long id;

    /** name. */
    private String name;

    /**
     *
     * @return employee id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id employee id
     */
    public void setId(Long id) {
        this.id = id;
    }

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
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
