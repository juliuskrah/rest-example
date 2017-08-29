package com.juliuskrah;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Resource, Long> {
}
