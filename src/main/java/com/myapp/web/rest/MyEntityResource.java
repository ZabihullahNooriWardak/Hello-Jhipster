package com.myapp.web.rest;

import com.myapp.repository.MyEntityRepository;
import com.myapp.service.MyEntityService;
import com.myapp.service.dto.MyEntityDTO;
import com.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.MyEntity}.
 */
@RestController
@RequestMapping("/api/my-entities")
public class MyEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(MyEntityResource.class);

    private static final String ENTITY_NAME = "myEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyEntityService myEntityService;

    private final MyEntityRepository myEntityRepository;

    public MyEntityResource(MyEntityService myEntityService, MyEntityRepository myEntityRepository) {
        this.myEntityService = myEntityService;
        this.myEntityRepository = myEntityRepository;
    }

    /**
     * {@code POST  /my-entities} : Create a new myEntity.
     *
     * @param myEntityDTO the myEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myEntityDTO, or with status {@code 400 (Bad Request)} if the myEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MyEntityDTO> createMyEntity(@Valid @RequestBody MyEntityDTO myEntityDTO) throws URISyntaxException {
        LOG.debug("REST request to save MyEntity : {}", myEntityDTO);
        if (myEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new myEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        myEntityDTO = myEntityService.save(myEntityDTO);
        return ResponseEntity.created(new URI("/api/my-entities/" + myEntityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, myEntityDTO.getId().toString()))
            .body(myEntityDTO);
    }

    /**
     * {@code PUT  /my-entities/:id} : Updates an existing myEntity.
     *
     * @param id the id of the myEntityDTO to save.
     * @param myEntityDTO the myEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myEntityDTO,
     * or with status {@code 400 (Bad Request)} if the myEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MyEntityDTO> updateMyEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MyEntityDTO myEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MyEntity : {}, {}", id, myEntityDTO);
        if (myEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        myEntityDTO = myEntityService.update(myEntityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myEntityDTO.getId().toString()))
            .body(myEntityDTO);
    }

    /**
     * {@code PATCH  /my-entities/:id} : Partial updates given fields of an existing myEntity, field will ignore if it is null
     *
     * @param id the id of the myEntityDTO to save.
     * @param myEntityDTO the myEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myEntityDTO,
     * or with status {@code 400 (Bad Request)} if the myEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the myEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the myEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MyEntityDTO> partialUpdateMyEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MyEntityDTO myEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MyEntity partially : {}, {}", id, myEntityDTO);
        if (myEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MyEntityDTO> result = myEntityService.partialUpdate(myEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myEntityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /my-entities} : get all the myEntities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myEntities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MyEntityDTO>> getAllMyEntities(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of MyEntities");
        Page<MyEntityDTO> page = myEntityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /my-entities/:id} : get the "id" myEntity.
     *
     * @param id the id of the myEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MyEntityDTO> getMyEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MyEntity : {}", id);
        Optional<MyEntityDTO> myEntityDTO = myEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(myEntityDTO);
    }

    /**
     * {@code DELETE  /my-entities/:id} : delete the "id" myEntity.
     *
     * @param id the id of the myEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MyEntity : {}", id);
        myEntityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
