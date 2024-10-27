package com.myapp.service;

import com.myapp.service.dto.MyEntityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.myapp.domain.MyEntity}.
 */
public interface MyEntityService {
    /**
     * Save a myEntity.
     *
     * @param myEntityDTO the entity to save.
     * @return the persisted entity.
     */
    MyEntityDTO save(MyEntityDTO myEntityDTO);

    /**
     * Updates a myEntity.
     *
     * @param myEntityDTO the entity to update.
     * @return the persisted entity.
     */
    MyEntityDTO update(MyEntityDTO myEntityDTO);

    /**
     * Partially updates a myEntity.
     *
     * @param myEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MyEntityDTO> partialUpdate(MyEntityDTO myEntityDTO);

    /**
     * Get all the myEntities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MyEntityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" myEntity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MyEntityDTO> findOne(Long id);

    /**
     * Delete the "id" myEntity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
