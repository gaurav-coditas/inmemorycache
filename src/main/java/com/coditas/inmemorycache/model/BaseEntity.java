package com.coditas.inmemorycache.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

    /** id. */
    private Long id;


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
}
