package com.eurodyn.qlack.be.forms.management.repository;

import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDefinitionAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrbeonFormDefinitionAttachmentRepository extends
    JpaRepository<OrbeonFormDefinitionAttachment, String>,
    QuerydslPredicateExecutor<OrbeonFormDefinitionAttachment> {

}
