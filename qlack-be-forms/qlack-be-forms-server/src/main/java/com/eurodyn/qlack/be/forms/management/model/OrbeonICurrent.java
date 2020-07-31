package com.eurodyn.qlack.be.forms.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "orbeon_i_current")
public class OrbeonICurrent {

  @Id
  @Column(name = "data_id")
  private Long dataId;

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
  private Long organizationId;
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
