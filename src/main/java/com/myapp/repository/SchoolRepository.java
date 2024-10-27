package com.myapp.repository;

import com.myapp.domain.School;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the School entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {}
