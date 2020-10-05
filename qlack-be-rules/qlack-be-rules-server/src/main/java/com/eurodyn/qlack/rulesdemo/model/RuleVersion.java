package com.eurodyn.qlack.rulesdemo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
