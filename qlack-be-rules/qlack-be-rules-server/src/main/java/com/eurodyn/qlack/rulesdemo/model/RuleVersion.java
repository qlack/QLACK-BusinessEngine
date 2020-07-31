package com.eurodyn.qlack.rulesdemo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rule_version")
public class RuleVersion extends BaseEntity{

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private Rule rule;

    @Column(name = "dmn_xml")
    private String dmnXml;

}
