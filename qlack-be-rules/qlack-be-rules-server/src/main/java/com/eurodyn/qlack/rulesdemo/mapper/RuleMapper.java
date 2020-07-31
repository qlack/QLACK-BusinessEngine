package com.eurodyn.qlack.rulesdemo.mapper;

import com.eurodyn.qlack.rulesdemo.dto.RuleDTO;
import com.eurodyn.qlack.rulesdemo.model.Rule;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RuleMapper extends BaseMapper<RuleDTO, Rule>  {
}
