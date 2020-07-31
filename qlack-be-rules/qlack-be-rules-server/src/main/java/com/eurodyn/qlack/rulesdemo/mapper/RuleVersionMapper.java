package com.eurodyn.qlack.rulesdemo.mapper;

import com.eurodyn.qlack.rulesdemo.dto.RuleVersionDTO;
import com.eurodyn.qlack.rulesdemo.model.RuleVersion;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = RuleMapper.class)
public abstract class RuleVersionMapper extends BaseMapper<RuleVersionDTO, RuleVersion> {
}
