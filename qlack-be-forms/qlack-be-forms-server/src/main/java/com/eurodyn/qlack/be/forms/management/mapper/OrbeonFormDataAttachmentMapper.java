package com.eurodyn.qlack.be.forms.management.mapper;

import com.eurodyn.qlack.be.forms.management.dto.OrbeonFormDataAttachmentDTO;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDataAttachment;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDataAttachmentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class OrbeonFormDataAttachmentMapper {

  @Autowired
  OrbeonFormDataAttachmentRepository repository;

  public abstract OrbeonFormDataAttachment map(OrbeonFormDataAttachmentDTO dto);

  public abstract OrbeonFormDataAttachmentDTO map(OrbeonFormDataAttachment entity);

  public abstract void map(OrbeonFormDataAttachmentDTO dto,
      @MappingTarget OrbeonFormDataAttachment entity);
}
