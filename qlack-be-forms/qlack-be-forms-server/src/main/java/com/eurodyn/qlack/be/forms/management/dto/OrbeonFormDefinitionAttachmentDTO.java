package com.eurodyn.qlack.be.forms.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrbeonFormDefinitionAttachmentDTO extends BaseDTO {

    private Instant created;
    private Instant lastModifiedTime;
    private String lastModifiedBy;
    private String app;
    private String form;
    private long formVersion;
    private String deleted = "N";
    private String fileName;
    private byte[] fileContent;
}

