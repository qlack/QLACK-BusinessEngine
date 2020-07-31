package com.eurodyn.qlack.be.forms.management.mapper;

import com.eurodyn.qlack.be.forms.management.dto.OrbeonIControlTextDTO;
import com.eurodyn.qlack.be.forms.management.model.OrbeonIControlText;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonIControlTextRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class OrbeonIControlTextMapper {

  @Autowired
  OrbeonIControlTextRepository repository;

  public abstract OrbeonIControlText map(OrbeonIControlTextDTO dto);

  public abstract OrbeonIControlTextDTO map(OrbeonIControlText entity);

  public abstract void map(OrbeonIControlTextDTO dto, @MappingTarget OrbeonIControlText entity);
}
