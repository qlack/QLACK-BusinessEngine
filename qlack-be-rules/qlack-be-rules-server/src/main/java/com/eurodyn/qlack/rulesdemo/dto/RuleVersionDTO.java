package com.eurodyn.qlack.rulesdemo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class RuleVersionDTO extends BaseDTO {

    private String name;

    private String description;

    private String rule;

    private String dmnXml;

}
