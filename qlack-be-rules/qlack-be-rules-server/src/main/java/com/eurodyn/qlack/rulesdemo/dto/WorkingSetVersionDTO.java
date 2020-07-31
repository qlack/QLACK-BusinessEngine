package com.eurodyn.qlack.rulesdemo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WorkingSetVersionDTO extends BaseDTO {

    private String name;

    private String description;

    private String workingSet;

    private List<RuleVersionDTO> ruleVersions;

}
