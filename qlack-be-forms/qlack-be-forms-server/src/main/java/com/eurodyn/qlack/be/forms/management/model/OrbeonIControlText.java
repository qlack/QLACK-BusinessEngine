package com.eurodyn.qlack.be.forms.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "orbeon_i_control_text")
public class OrbeonIControlText {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column
  private long dataId;
  @Column
  private long pos;
  @Column
  private String control;
  @Column
  private String val;
}
