package com.eurodyn.qlack.be.forms.management.repository;

import com.eurodyn.qlack.be.forms.management.model.OrbeonIControlText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrbeonIControlTextRepository extends JpaRepository<OrbeonIControlText, Long>,
    QuerydslPredicateExecutor<OrbeonIControlText> {

}
