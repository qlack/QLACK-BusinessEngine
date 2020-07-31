package com.eurodyn.qlack.be.forms.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

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
  @Column
  private Instant lastModifiedTime;
  @Column
  private String lastModifiedBy;
  @Column
  private String username;
  @Column
  private String groupname;
  @Column
  private Long organizationId;
  @Column
  private String app;
  @Column
  private String form;
  @Column
  private Long formVersion;
  @Column
  private String stage;
  @Column
  private String documentId;
  @Column
  private String draft = "N";
  @Column
  private String deleted = "N";
  @Column
  private String xml;
}
