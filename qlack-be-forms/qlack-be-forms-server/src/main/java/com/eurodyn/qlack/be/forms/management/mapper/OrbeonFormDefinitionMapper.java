package com.eurodyn.qlack.be.forms.management.mapper;

import com.eurodyn.qlack.be.forms.management.dto.OrbeonFormDefinitionDTO;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDefinition;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDefinitionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class OrbeonFormDefinitionMapper {

    @Autowired
    OrbeonFormDefinitionRepository repository;

    public abstract OrbeonFormDefinition map(OrbeonFormDefinitionDTO dto);

    public abstract OrbeonFormDefinitionDTO map(OrbeonFormDefinition entity);

    public abstract void map(OrbeonFormDefinitionDTO dto, @MappingTarget OrbeonFormDefinition entity);
}
