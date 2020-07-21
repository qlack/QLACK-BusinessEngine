package com.eurodyn.qlack.be.forms.management.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "orbeon_form_definition")
public class OrbeonFormDefinition {

    @Column
    @CreatedDate
    private Instant created;
    @Column(name = "last_modified_time")
    private Instant lastModifiedTime;
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    @Id
    @Column
    private String app;
    @Column
    private String form;

    @Column(name = "form_version")
    private long formVersion;
    @Column(name = "form_metadata")
    private String formMetadata;
    @Column
    private String deleted = "N";
    @Column
    private String xml;
}

