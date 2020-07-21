package com.eurodyn.qlack.be.forms.management.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "orbeon_i_control_text")
public class OrbeonIControlText {

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
    private long pos;
    @Column
    private String control;
    @Column
    private String val;
}
