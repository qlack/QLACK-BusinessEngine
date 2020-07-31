package com.eurodyn.qlack.be.forms.management.mapper;

import com.eurodyn.qlack.be.forms.management.dto.OrbeonFormDefinitionAttachmentDTO;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDefinitionAttachment;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDefinitionAttachmentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class OrbeonFormDefinitionAttachmentMapper {

  @Autowired
  OrbeonFormDefinitionAttachmentRepository repository;

  public abstract OrbeonFormDefinitionAttachment map(OrbeonFormDefinitionAttachmentDTO dto);

  public abstract OrbeonFormDefinitionAttachmentDTO map(OrbeonFormDefinitionAttachment entity);

  public abstract void map(OrbeonFormDefinitionAttachmentDTO dto,
      @MappingTarget OrbeonFormDefinitionAttachment entity);
}