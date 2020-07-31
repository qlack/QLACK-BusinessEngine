package com.eurodyn.qlack.rulesdemo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "working_set_version")
public class WorkingSetVersion extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "working_set_id")
    private WorkingSet workingSet;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "working_set_version_rule_version",
        joinColumns = @JoinColumn(name = "working_set_version_id"),
        inverseJoinColumns = @JoinColumn(name = "rule_version_id"))
    private Set<RuleVersion> ruleVersions = new HashSet<>();
}
