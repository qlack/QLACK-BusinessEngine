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
@Table(name = "working_set")
public class WorkingSet extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private boolean status;

    @OneToMany(mappedBy = "workingSet", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<WorkingSetVersion> workingSetVersions;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "working_set_category",
            joinColumns = @JoinColumn(name = "working_set_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkingSet)) return false;
        WorkingSet workingSet = (WorkingSet) o;
        return Objects.equals(getId(), workingSet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
