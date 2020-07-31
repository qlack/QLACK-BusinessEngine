package com.eurodyn.qlack.be.forms.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrbeonIControlTextDTO extends BaseDTO {

  private long data_id;

  private Long version;

  private long pos;
  private String control;
  private String val;
}
