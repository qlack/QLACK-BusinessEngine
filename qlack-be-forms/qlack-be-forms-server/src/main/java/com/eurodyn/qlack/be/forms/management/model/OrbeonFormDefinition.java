package com.eurodyn.qlack.be.forms.management.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "orbeon_form_definition")
public class OrbeonFormDefinition implements Serializable {
  @EmbeddedId
  private OrbeonFormDefinitionPK orbeonFormDefinitionPK;

  @Column
  @CreatedDate
  private Instant created;
  @Column
  private Instant lastModifiedTime;
  @Column
  private String lastModifiedBy;
  @Column
  private String formMetadata;
  @Column
  private String deleted = "N";
  @Column
  private String xml;
}

