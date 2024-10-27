package com.myapp.repository;

import com.myapp.domain.StudentDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudentDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentDetailsRepository extends JpaRepository<StudentDetails, Long> {}
