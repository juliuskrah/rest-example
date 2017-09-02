package com.juliuskrah;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PersonRepository extends JpaRepository<Resource, Long>, QueryDslPredicateExecutor<Resource> {
}
