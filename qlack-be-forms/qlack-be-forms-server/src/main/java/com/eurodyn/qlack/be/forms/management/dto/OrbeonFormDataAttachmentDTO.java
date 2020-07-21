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
public class OrbeonFormDataAttachmentDTO extends BaseDTO {
    private Instant created;
    private Instant lastModifiedTime;
    private String lastModifiedBy;
    private String username;
    private String groupname;
    private Long organizationId;
    private String app;
    private String form;
    private Long formVersion;
    private String documentId;
    private String draft = "N";
    private String deleted;
    private String fileName;
    private byte[] fileContent;
}

