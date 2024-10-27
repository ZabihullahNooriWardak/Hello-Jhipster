package com.myapp.web.rest;

import static com.myapp.domain.StudentDetailsAsserts.*;
import static com.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.IntegrationTest;
import com.myapp.domain.StudentDetails;
import com.myapp.repository.StudentDetailsRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StudentDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentDetailsResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/student-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudentDetailsRepository studentDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentDetailsMockMvc;

    private StudentDetails studentDetails;

    private StudentDetails insertedStudentDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentDetails createEntity() {
        return new StudentDetails().phone(DEFAULT_PHONE).email(DEFAULT_EMAIL).address(DEFAULT_ADDRESS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentDetails createUpdatedEntity() {
        return new StudentDetails().phone(UPDATED_PHONE).email(UPDATED_EMAIL).address(UPDATED_ADDRESS);
    }

    @BeforeEach
    public void initTest() {
        studentDetails = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStudentDetails != null) {
            studentDetailsRepository.delete(insertedStudentDetails);
            insertedStudentDetails = null;
        }
    }

    @Test
    @Transactional
    void createStudentDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StudentDetails
        var returnedStudentDetails = om.readValue(
            restStudentDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentDetails)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StudentDetails.class
        );

        // Validate the StudentDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStudentDetailsUpdatableFieldsEquals(returnedStudentDetails, getPersistedStudentDetails(returnedStudentDetails));

        insertedStudentDetails = returnedStudentDetails;
    }

    @Test
    @Transactional
    void createStudentDetailsWithExistingId() throws Exception {
        // Create the StudentDetails with an existing ID
        studentDetails.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentDetails)))
            .andExpect(status().isBadRequest());

        // Validate the StudentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudentDetails() throws Exception {
        // Initialize the database
        insertedStudentDetails = studentDetailsRepository.saveAndFlush(studentDetails);

        // Get all the studentDetailsList
        restStudentDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getStudentDetails() throws Exception {
        // Initialize the database
        insertedStudentDetails = studentDetailsRepository.saveAndFlush(studentDetails);

        // Get the studentDetails
        restStudentDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, studentDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentDetails.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingStudentDetails() throws Exception {
        // Get the studentDetails
        restStudentDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudentDetails() throws Exception {
        // Initialize the database
        insertedStudentDetails = studentDetailsRepository.saveAndFlush(studentDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentDetails
        StudentDetails updatedStudentDetails = studentDetailsRepository.findById(studentDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudentDetails are not directly saved in db
        em.detach(updatedStudentDetails);
        updatedStudentDetails.phone(UPDATED_PHONE).email(UPDATED_EMAIL).address(UPDATED_ADDRESS);

        restStudentDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudentDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStudentDetails))
            )
            .andExpect(status().isOk());

        // Validate the StudentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStudentDetailsToMatchAllProperties(updatedStudentDetails);
    }

    @Test
    @Transactional
    void putNonExistingStudentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedStudentDetails = studentDetailsRepository.saveAndFlush(studentDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentDetails using partial update
        StudentDetails partialUpdatedStudentDetails = new StudentDetails();
        partialUpdatedStudentDetails.setId(studentDetails.getId());

        partialUpdatedStudentDetails.email(UPDATED_EMAIL);

        restStudentDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentDetails))
            )
            .andExpect(status().isOk());

        // Validate the StudentDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStudentDetails, studentDetails),
            getPersistedStudentDetails(studentDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateStudentDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedStudentDetails = studentDetailsRepository.saveAndFlush(studentDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentDetails using partial update
        StudentDetails partialUpdatedStudentDetails = new StudentDetails();
        partialUpdatedStudentDetails.setId(studentDetails.getId());

        partialUpdatedStudentDetails.phone(UPDATED_PHONE).email(UPDATED_EMAIL).address(UPDATED_ADDRESS);

        restStudentDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentDetails))
            )
            .andExpect(status().isOk());

        // Validate the StudentDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentDetailsUpdatableFieldsEquals(partialUpdatedStudentDetails, getPersistedStudentDetails(partialUpdatedStudentDetails));
    }

    @Test
    @Transactional
    void patchNonExistingStudentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(studentDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentDetails() throws Exception {
        // Initialize the database
        insertedStudentDetails = studentDetailsRepository.saveAndFlush(studentDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the studentDetails
        restStudentDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return studentDetailsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected StudentDetails getPersistedStudentDetails(StudentDetails studentDetails) {
        return studentDetailsRepository.findById(studentDetails.getId()).orElseThrow();
    }

    protected void assertPersistedStudentDetailsToMatchAllProperties(StudentDetails expectedStudentDetails) {
        assertStudentDetailsAllPropertiesEquals(expectedStudentDetails, getPersistedStudentDetails(expectedStudentDetails));
    }

    protected void assertPersistedStudentDetailsToMatchUpdatableProperties(StudentDetails expectedStudentDetails) {
        assertStudentDetailsAllUpdatablePropertiesEquals(expectedStudentDetails, getPersistedStudentDetails(expectedStudentDetails));
    }
}
