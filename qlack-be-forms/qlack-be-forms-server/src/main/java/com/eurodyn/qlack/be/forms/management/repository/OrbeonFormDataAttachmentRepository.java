package com.eurodyn.qlack.be.forms.management.repository;

import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDataAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrbeonFormDataAttachmentRepository extends JpaRepository<OrbeonFormDataAttachment, String>,
        QuerydslPredicateExecutor<OrbeonFormDataAttachment> {

}
