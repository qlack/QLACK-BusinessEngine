package com.eurodyn.qlack.be.forms.management.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "orbeon_i_current")
public class OrbeonICurrent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id")
    private long data_id;

//    @CreatedDate
//    @Column(name = "created_on", updatable = false, nullable = false)
//    private Instant createdOn;
//
//    @CreatedBy
//    @Column(name = "created_by", updatable = false, nullable = false)
//    private String createdBy;
//
//    @LastModifiedDate
//    @Column(name = "modified_on")
//    private Instant modifiedOn;
//
//    @LastModifiedBy
//    private String modifiedBy;
//
//    @Version
//    private Long version;

    @Column
    @CreatedDate
    private Instant created;
    @Column
    @LastModifiedDate
    private Instant lastModifiedTime;
    @Column
    @LastModifiedBy
    private String lastModifiedBy;
    @Column
    private String username;
    @Column
    private String groupname;
    @Column
    private long organizationId;
    @Column
    private String app;
    @Column
    private String form;
    @Column
    private long formVersion;
    @Column
    private String stage;
    @Column
    private String documentId;
    @Column
    private String draft = "N";
}
