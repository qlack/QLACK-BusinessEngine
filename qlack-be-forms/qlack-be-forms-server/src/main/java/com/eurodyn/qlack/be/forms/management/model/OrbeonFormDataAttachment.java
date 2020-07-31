package com.eurodyn.qlack.be.forms.management.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "orbeon_form_data_attach")
public class OrbeonFormDataAttachment {

  @EmbeddedId
  private OrbeonFormDataAttachmentPK orbeonFormDataAttachmentPK;
  @Column
  private String documentId;
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
  private Long formVersion;
  @Column
  private String draft = "N";
  @Column
  private String deleted = "N";
  @Column
  private byte[] fileContent;
}
