package com.myapp.web.rest;

import static com.myapp.domain.StudClassAsserts.*;
import static com.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.IntegrationTest;
import com.myapp.domain.StudClass;
import com.myapp.repository.StudClassRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StudClassResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StudClassResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stud-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudClassRepository studClassRepository;

    @Mock
    private StudClassRepository studClassRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudClassMockMvc;

    private StudClass studClass;

    private StudClass insertedStudClass;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudClass createEntity() {
        return new StudClass().className(DEFAULT_CLASS_NAME).subject(DEFAULT_SUBJECT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudClass createUpdatedEntity() {
        return new StudClass().className(UPDATED_CLASS_NAME).subject(UPDATED_SUBJECT);
    }

    @BeforeEach
    public void initTest() {
        studClass = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStudClass != null) {
            studClassRepository.delete(insertedStudClass);
            insertedStudClass = null;
        }
    }

    @Test
    @Transactional
    void createStudClass() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StudClass
        var returnedStudClass = om.readValue(
            restStudClassMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studClass)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StudClass.class
        );

        // Validate the StudClass in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStudClassUpdatableFieldsEquals(returnedStudClass, getPersistedStudClass(returnedStudClass));

        insertedStudClass = returnedStudClass;
    }

    @Test
    @Transactional
    void createStudClassWithExistingId() throws Exception {
        // Create the StudClass with an existing ID
        studClass.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudClassMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studClass)))
            .andExpect(status().isBadRequest());

        // Validate the StudClass in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudClasses() throws Exception {
        // Initialize the database
        insertedStudClass = studClassRepository.saveAndFlush(studClass);

        // Get all the studClassList
        restStudClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStudClassesWithEagerRelationshipsIsEnabled() throws Exception {
        when(studClassRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudClassMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(studClassRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStudClassesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(studClassRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudClassMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(studClassRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStudClass() throws Exception {
        // Initialize the database
        insertedStudClass = studClassRepository.saveAndFlush(studClass);

        // Get the studClass
        restStudClassMockMvc
            .perform(get(ENTITY_API_URL_ID, studClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studClass.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT));
    }

    @Test
    @Transactional
    void getNonExistingStudClass() throws Exception {
        // Get the studClass
        restStudClassMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudClass() throws Exception {
        // Initialize the database
        insertedStudClass = studClassRepository.saveAndFlush(studClass);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studClass
        StudClass updatedStudClass = studClassRepository.findById(studClass.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudClass are not directly saved in db
        em.detach(updatedStudClass);
        updatedStudClass.className(UPDATED_CLASS_NAME).subject(UPDATED_SUBJECT);

        restStudClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudClass.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStudClass))
            )
            .andExpect(status().isOk());

        // Validate the StudClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStudClassToMatchAllProperties(updatedStudClass);
    }

    @Test
    @Transactional
    void putNonExistingStudClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studClass.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studClass.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studClass))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studClass.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studClass))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studClass.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudClassMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studClass)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudClassWithPatch() throws Exception {
        // Initialize the database
        insertedStudClass = studClassRepository.saveAndFlush(studClass);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studClass using partial update
        StudClass partialUpdatedStudClass = new StudClass();
        partialUpdatedStudClass.setId(studClass.getId());

        partialUpdatedStudClass.className(UPDATED_CLASS_NAME);

        restStudClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudClass))
            )
            .andExpect(status().isOk());

        // Validate the StudClass in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudClassUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStudClass, studClass),
            getPersistedStudClass(studClass)
        );
    }

    @Test
    @Transactional
    void fullUpdateStudClassWithPatch() throws Exception {
        // Initialize the database
        insertedStudClass = studClassRepository.saveAndFlush(studClass);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studClass using partial update
        StudClass partialUpdatedStudClass = new StudClass();
        partialUpdatedStudClass.setId(studClass.getId());

        partialUpdatedStudClass.className(UPDATED_CLASS_NAME).subject(UPDATED_SUBJECT);

        restStudClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudClass))
            )
            .andExpect(status().isOk());

        // Validate the StudClass in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudClassUpdatableFieldsEquals(partialUpdatedStudClass, getPersistedStudClass(partialUpdatedStudClass));
    }

    @Test
    @Transactional
    void patchNonExistingStudClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studClass.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studClass))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studClass.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studClass))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studClass.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudClassMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(studClass)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudClass() throws Exception {
        // Initialize the database
        insertedStudClass = studClassRepository.saveAndFlush(studClass);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the studClass
        restStudClassMockMvc
            .perform(delete(ENTITY_API_URL_ID, studClass.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return studClassRepository.count();
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

    protected StudClass getPersistedStudClass(StudClass studClass) {
        return studClassRepository.findById(studClass.getId()).orElseThrow();
    }

    protected void assertPersistedStudClassToMatchAllProperties(StudClass expectedStudClass) {
        assertStudClassAllPropertiesEquals(expectedStudClass, getPersistedStudClass(expectedStudClass));
    }

    protected void assertPersistedStudClassToMatchUpdatableProperties(StudClass expectedStudClass) {
        assertStudClassAllUpdatablePropertiesEquals(expectedStudClass, getPersistedStudClass(expectedStudClass));
    }
}
