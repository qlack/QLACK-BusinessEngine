package com.eurodyn.qlack.be.forms.management.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

  @Data
  @Embeddable
  @NoArgsConstructor
  @AllArgsConstructor
  @Accessors(chain = true)
  public class OrbeonFormDataAttachmentPK implements Serializable {
    @Column
    private String app;
    @Column
    private String form;
    @Column
    private String fileName;
  }
