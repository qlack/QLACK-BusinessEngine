package com.eurodyn.qlack.be.forms.management.mapper;

import com.eurodyn.qlack.be.forms.management.dto.OrbeonICurrentDTO;
import com.eurodyn.qlack.be.forms.management.model.OrbeonICurrent;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonICurrentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class OrbeonICurrentMapper {

    @Autowired
    OrbeonICurrentRepository repository;

    public abstract OrbeonICurrent map(OrbeonICurrentDTO dto);

    public abstract OrbeonICurrentDTO map(OrbeonICurrent entity);

    public abstract void map(OrbeonICurrentDTO dto, @MappingTarget OrbeonICurrent entity);
}
