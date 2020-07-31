package com.eurodyn.qlack.rulesdemo.repository;

import com.eurodyn.qlack.rulesdemo.model.BaseEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface BaseRepository<M extends BaseEntity> extends JpaRepository<M, String>,
    QuerydslPredicateExecutor<M> {

}
