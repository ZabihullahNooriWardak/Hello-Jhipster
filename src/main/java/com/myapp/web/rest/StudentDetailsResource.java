package com.myapp.web.rest;

import com.myapp.domain.StudentDetails;
import com.myapp.repository.StudentDetailsRepository;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.StudentDetails}.
 */
@RestController
@RequestMapping("/api/student-details")
@Transactional
public class StudentDetailsResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentDetailsResource.class);

    private static final String ENTITY_NAME = "studentDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentDetailsRepository studentDetailsRepository;

    public StudentDetailsResource(StudentDetailsRepository studentDetailsRepository) {
        this.studentDetailsRepository = studentDetailsRepository;
    }

    /**
     * {@code POST  /student-details} : Create a new studentDetails.
     *
     * @param studentDetails the studentDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentDetails, or with status {@code 400 (Bad Request)} if the studentDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudentDetails> createStudentDetails(@RequestBody StudentDetails studentDetails) throws URISyntaxException {
        LOG.debug("REST request to save StudentDetails : {}", studentDetails);
        if (studentDetails.getId() != null) {
            throw new BadRequestAlertException("A new studentDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studentDetails = studentDetailsRepository.save(studentDetails);
        return ResponseEntity.created(new URI("/api/student-details/" + studentDetails.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studentDetails.getId().toString()))
            .body(studentDetails);
    }

    /**
     * {@code PUT  /student-details/:id} : Updates an existing studentDetails.
     *
     * @param id the id of the studentDetails to save.
     * @param studentDetails the studentDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentDetails,
     * or with status {@code 400 (Bad Request)} if the studentDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentDetails> updateStudentDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentDetails studentDetails
    ) throws URISyntaxException {
        LOG.debug("REST request to update StudentDetails : {}, {}", id, studentDetails);
        if (studentDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studentDetails = studentDetailsRepository.save(studentDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentDetails.getId().toString()))
            .body(studentDetails);
    }

    /**
     * {@code PATCH  /student-details/:id} : Partial updates given fields of an existing studentDetails, field will ignore if it is null
     *
     * @param id the id of the studentDetails to save.
     * @param studentDetails the studentDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentDetails,
     * or with status {@code 400 (Bad Request)} if the studentDetails is not valid,
     * or with status {@code 404 (Not Found)} if the studentDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentDetails> partialUpdateStudentDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentDetails studentDetails
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StudentDetails partially : {}, {}", id, studentDetails);
        if (studentDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentDetails> result = studentDetailsRepository
            .findById(studentDetails.getId())
            .map(existingStudentDetails -> {
                if (studentDetails.getPhone() != null) {
                    existingStudentDetails.setPhone(studentDetails.getPhone());
                }
                if (studentDetails.getEmail() != null) {
                    existingStudentDetails.setEmail(studentDetails.getEmail());
                }
                if (studentDetails.getAddress() != null) {
                    existingStudentDetails.setAddress(studentDetails.getAddress());
                }

                return existingStudentDetails;
            })
            .map(studentDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /student-details} : get all the studentDetails.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentDetails in body.
     */
    @GetMapping("")
    public List<StudentDetails> getAllStudentDetails(@RequestParam(name = "filter", required = false) String filter) {
        if ("student-is-null".equals(filter)) {
            LOG.debug("REST request to get all StudentDetailss where student is null");
            return StreamSupport.stream(studentDetailsRepository.findAll().spliterator(), false)
                .filter(studentDetails -> studentDetails.getStudent() == null)
                .toList();
        }
        LOG.debug("REST request to get all StudentDetails");
        return studentDetailsRepository.findAll();
    }

    /**
     * {@code GET  /student-details/:id} : get the "id" studentDetails.
     *
     * @param id the id of the studentDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDetails> getStudentDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StudentDetails : {}", id);
        Optional<StudentDetails> studentDetails = studentDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studentDetails);
    }

    /**
     * {@code DELETE  /student-details/:id} : delete the "id" studentDetails.
     *
     * @param id the id of the studentDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StudentDetails : {}", id);
        studentDetailsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
