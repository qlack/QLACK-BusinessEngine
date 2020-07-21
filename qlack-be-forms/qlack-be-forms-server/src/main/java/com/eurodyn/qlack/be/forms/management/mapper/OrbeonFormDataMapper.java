package com.eurodyn.qlack.be.forms.management.mapper;

import com.eurodyn.qlack.be.forms.management.dto.OrbeonFormDataDTO;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormData;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDataRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class OrbeonFormDataMapper {

    @Autowired
    OrbeonFormDataRepository repository;

    public abstract OrbeonFormData map(OrbeonFormDataDTO dto);

    public abstract OrbeonFormDataDTO map(OrbeonFormData entity);

    public abstract void map(OrbeonFormDataDTO dto, @MappingTarget OrbeonFormData entity);
}
