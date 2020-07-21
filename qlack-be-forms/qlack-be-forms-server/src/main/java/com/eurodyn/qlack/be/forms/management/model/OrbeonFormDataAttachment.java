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
@Accessors(chain = true)
@Entity
@Table(name = "orbeon_form_data_attach")
public class OrbeonFormDataAttachment {

    @Id
    @Column(name = "document_id")
    private String documentId;
    @Column
    @CreatedDate
    private Instant created;
    @Column(name = "last_modified_time")
    private Instant lastModifiedTime;
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    @Column
    private String username;
    @Column
    private String groupname;
    @Column(name = "organization_id")
    private Long organizationId;
    @Column
    private String app;
    @Column
    private String form;
    @Column(name = "form_version")
    private Long formVersion;
    @Column
    private String draft = "N";
    @Column
    private String deleted = "N";
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_content")
    private byte[] fileContent;
}
