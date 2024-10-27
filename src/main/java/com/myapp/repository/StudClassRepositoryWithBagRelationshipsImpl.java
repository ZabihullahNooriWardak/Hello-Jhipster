package com.myapp.repository;

import com.myapp.domain.StudClass;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class StudClassRepositoryWithBagRelationshipsImpl implements StudClassRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String STUDCLASSES_PARAMETER = "studClasses";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<StudClass> fetchBagRelationships(Optional<StudClass> studClass) {
        return studClass.map(this::fetchStudents);
    }

    @Override
    public Page<StudClass> fetchBagRelationships(Page<StudClass> studClasses) {
        return new PageImpl<>(fetchBagRelationships(studClasses.getContent()), studClasses.getPageable(), studClasses.getTotalElements());
    }

    @Override
    public List<StudClass> fetchBagRelationships(List<StudClass> studClasses) {
        return Optional.of(studClasses).map(this::fetchStudents).orElse(Collections.emptyList());
    }

    StudClass fetchStudents(StudClass result) {
        return entityManager
            .createQuery(
                "select studClass from StudClass studClass left join fetch studClass.students where studClass.id = :id",
                StudClass.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<StudClass> fetchStudents(List<StudClass> studClasses) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, studClasses.size()).forEach(index -> order.put(studClasses.get(index).getId(), index));
        List<StudClass> result = entityManager
            .createQuery(
                "select studClass from StudClass studClass left join fetch studClass.students where studClass in :studClasses",
                StudClass.class
            )
            .setParameter(STUDCLASSES_PARAMETER, studClasses)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
