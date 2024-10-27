package com.myapp.repository;

import com.myapp.domain.StudClass;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface StudClassRepositoryWithBagRelationships {
    Optional<StudClass> fetchBagRelationships(Optional<StudClass> studClass);

    List<StudClass> fetchBagRelationships(List<StudClass> studClasses);

    Page<StudClass> fetchBagRelationships(Page<StudClass> studClasses);
}
