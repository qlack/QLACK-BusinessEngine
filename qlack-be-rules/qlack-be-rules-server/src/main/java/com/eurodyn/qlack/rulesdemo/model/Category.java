package com.eurodyn.qlack.rulesdemo.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Rule> rules = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    private Set<WorkingSet> workingSets = new HashSet<>();
}
