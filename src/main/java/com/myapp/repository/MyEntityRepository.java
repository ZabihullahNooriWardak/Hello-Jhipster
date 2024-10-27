package com.myapp.repository;

import com.myapp.domain.MyEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MyEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {}
