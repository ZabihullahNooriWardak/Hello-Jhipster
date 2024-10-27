package com.myapp.web.rest;

import com.myapp.domain.StudClass;
import com.myapp.repository.StudClassRepository;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.StudClass}.
 */
@RestController
@RequestMapping("/api/stud-classes")
@Transactional
public class StudClassResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudClassResource.class);

    private static final String ENTITY_NAME = "studClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudClassRepository studClassRepository;

    public StudClassResource(StudClassRepository studClassRepository) {
        this.studClassRepository = studClassRepository;
    }

    /**
     * {@code POST  /stud-classes} : Create a new studClass.
     *
     * @param studClass the studClass to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studClass, or with status {@code 400 (Bad Request)} if the studClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudClass> createStudClass(@RequestBody StudClass studClass) throws URISyntaxException {
        LOG.debug("REST request to save StudClass : {}", studClass);
        if (studClass.getId() != null) {
            throw new BadRequestAlertException("A new studClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studClass = studClassRepository.save(studClass);
        return ResponseEntity.created(new URI("/api/stud-classes/" + studClass.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studClass.getId().toString()))
            .body(studClass);
    }

    /**
     * {@code PUT  /stud-classes/:id} : Updates an existing studClass.
     *
     * @param id the id of the studClass to save.
     * @param studClass the studClass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studClass,
     * or with status {@code 400 (Bad Request)} if the studClass is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studClass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudClass> updateStudClass(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudClass studClass
    ) throws URISyntaxException {
        LOG.debug("REST request to update StudClass : {}, {}", id, studClass);
        if (studClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studClass.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studClass = studClassRepository.save(studClass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studClass.getId().toString()))
            .body(studClass);
    }

    /**
     * {@code PATCH  /stud-classes/:id} : Partial updates given fields of an existing studClass, field will ignore if it is null
     *
     * @param id the id of the studClass to save.
     * @param studClass the studClass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studClass,
     * or with status {@code 400 (Bad Request)} if the studClass is not valid,
     * or with status {@code 404 (Not Found)} if the studClass is not found,
     * or with status {@code 500 (Internal Server Error)} if the studClass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudClass> partialUpdateStudClass(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudClass studClass
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StudClass partially : {}, {}", id, studClass);
        if (studClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studClass.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudClass> result = studClassRepository
            .findById(studClass.getId())
            .map(existingStudClass -> {
                if (studClass.getClassName() != null) {
                    existingStudClass.setClassName(studClass.getClassName());
                }
                if (studClass.getSubject() != null) {
                    existingStudClass.setSubject(studClass.getSubject());
                }

                return existingStudClass;
            })
            .map(studClassRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studClass.getId().toString())
        );
    }

    /**
     * {@code GET  /stud-classes} : get all the studClasses.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studClasses in body.
     */
    @GetMapping("")
    public List<StudClass> getAllStudClasses(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all StudClasses");
        if (eagerload) {
            return studClassRepository.findAllWithEagerRelationships();
        } else {
            return studClassRepository.findAll();
        }
    }

    /**
     * {@code GET  /stud-classes/:id} : get the "id" studClass.
     *
     * @param id the id of the studClass to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studClass, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudClass> getStudClass(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StudClass : {}", id);
        Optional<StudClass> studClass = studClassRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(studClass);
    }

    /**
     * {@code DELETE  /stud-classes/:id} : delete the "id" studClass.
     *
     * @param id the id of the studClass to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudClass(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StudClass : {}", id);
        studClassRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
