package com.eurodyn.qlack.be.forms.management.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "orbeon_form_data")
public class OrbeonFormData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
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
    private String stage;
    @Column(name = "document_id")
    private String documentId;
    @Column
    private String draft = "N";
    @Column
    private String deleted = "N";
    @Column
    private String xml;
}
