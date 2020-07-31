package com.eurodyn.qlack.rulesdemo.mapper;

import com.eurodyn.qlack.rulesdemo.dto.WorkingSetVersionDTO;
import com.eurodyn.qlack.rulesdemo.model.WorkingSetVersion;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {RuleVersionMapper.class, RuleMapper.class, WorkingSetMapper.class})
public abstract class WorkingSetVersionMapper extends BaseMapper<WorkingSetVersionDTO, WorkingSetVersion> {
}
