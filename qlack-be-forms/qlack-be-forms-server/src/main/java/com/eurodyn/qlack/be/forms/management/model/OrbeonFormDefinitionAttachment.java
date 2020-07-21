package com.eurodyn.qlack.be.forms.management.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "orbeon_form_definition_attach")
public class OrbeonFormDefinitionAttachment {

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
    @Column
    private String deleted = "N";
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_content")
    private byte[] fileContent;
}

