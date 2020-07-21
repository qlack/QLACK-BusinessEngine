package com.eurodyn.qlack.be.forms.management.repository;

import com.eurodyn.qlack.be.forms.management.model.OrbeonFormData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrbeonFormDataRepository extends JpaRepository<OrbeonFormData, String>,
        QuerydslPredicateExecutor<OrbeonFormData> {

}
