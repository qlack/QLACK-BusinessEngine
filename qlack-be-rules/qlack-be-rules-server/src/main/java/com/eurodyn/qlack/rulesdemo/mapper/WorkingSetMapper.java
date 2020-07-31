package com.eurodyn.qlack.rulesdemo.mapper;

import com.eurodyn.qlack.rulesdemo.dto.WorkingSetDTO;
import com.eurodyn.qlack.rulesdemo.model.WorkingSet;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {RuleMapper.class, RuleVersionMapper.class})
public abstract class WorkingSetMapper extends BaseMapper<WorkingSetDTO, WorkingSet> {
}
