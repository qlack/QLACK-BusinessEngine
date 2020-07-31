package com.eurodyn.qlack.rulesdemo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class RuleDTO extends BaseDTO {

    private String name;

    private String description;

    private boolean status;

    private List<RuleVersionDTO> ruleVersions;

    private List<CategoryDTO> categories;
}
