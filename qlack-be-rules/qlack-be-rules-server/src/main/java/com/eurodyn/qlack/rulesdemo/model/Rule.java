package com.eurodyn.qlack.rulesdemo.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rule")
public class Rule extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private boolean status;

    @OneToMany(mappedBy = "rule", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<RuleVersion> ruleVersions;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "rule_category",
            joinColumns = @JoinColumn(name = "rule_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rule)) return false;
        Rule rule = (Rule) o;
        return Objects.equals(getId(), rule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
