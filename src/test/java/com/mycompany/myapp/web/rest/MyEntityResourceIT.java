package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MyEntityAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MyEntity;
import com.mycompany.myapp.repository.MyEntityRepository;
import com.mycompany.myapp.service.dto.MyEntityDTO;
import com.mycompany.myapp.service.mapper.MyEntityMapper;
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
 * Integration tests for the {@link MyEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MyEntityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/my-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MyEntityRepository myEntityRepository;

    @Autowired
    private MyEntityMapper myEntityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyEntityMockMvc;

    private MyEntity myEntity;

    private MyEntity insertedMyEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyEntity createEntity() {
        return new MyEntity().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyEntity createUpdatedEntity() {
        return new MyEntity().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        myEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMyEntity != null) {
            myEntityRepository.delete(insertedMyEntity);
            insertedMyEntity = null;
        }
    }

    @Test
    @Transactional
    void createMyEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MyEntity
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(myEntity);
        var returnedMyEntityDTO = om.readValue(
            restMyEntityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myEntityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MyEntityDTO.class
        );

        // Validate the MyEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMyEntity = myEntityMapper.toEntity(returnedMyEntityDTO);
        assertMyEntityUpdatableFieldsEquals(returnedMyEntity, getPersistedMyEntity(returnedMyEntity));

        insertedMyEntity = returnedMyEntity;
    }

    @Test
    @Transactional
    void createMyEntityWithExistingId() throws Exception {
        // Create the MyEntity with an existing ID
        myEntity.setId(1L);
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(myEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myEntityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MyEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        myEntity.setName(null);

        // Create the MyEntity, which fails.
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(myEntity);

        restMyEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myEntityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMyEntities() throws Exception {
        // Initialize the database
        insertedMyEntity = myEntityRepository.saveAndFlush(myEntity);

        // Get all the myEntityList
        restMyEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getMyEntity() throws Exception {
        // Initialize the database
        insertedMyEntity = myEntityRepository.saveAndFlush(myEntity);

        // Get the myEntity
        restMyEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, myEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMyEntity() throws Exception {
        // Get the myEntity
        restMyEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMyEntity() throws Exception {
        // Initialize the database
        insertedMyEntity = myEntityRepository.saveAndFlush(myEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myEntity
        MyEntity updatedMyEntity = myEntityRepository.findById(myEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMyEntity are not directly saved in db
        em.detach(updatedMyEntity);
        updatedMyEntity.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(updatedMyEntity);

        restMyEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, myEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(myEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the MyEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMyEntityToMatchAllProperties(updatedMyEntity);
    }

    @Test
    @Transactional
    void putNonExistingMyEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myEntity.setId(longCount.incrementAndGet());

        // Create the MyEntity
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(myEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, myEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(myEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMyEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myEntity.setId(longCount.incrementAndGet());

        // Create the MyEntity
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(myEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(myEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMyEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myEntity.setId(longCount.incrementAndGet());

        // Create the MyEntity
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(myEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyEntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myEntityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMyEntityWithPatch() throws Exception {
        // Initialize the database
        insertedMyEntity = myEntityRepository.saveAndFlush(myEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myEntity using partial update
        MyEntity partialUpdatedMyEntity = new MyEntity();
        partialUpdatedMyEntity.setId(myEntity.getId());

        partialUpdatedMyEntity.name(UPDATED_NAME);

        restMyEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMyEntity))
            )
            .andExpect(status().isOk());

        // Validate the MyEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMyEntityUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMyEntity, myEntity), getPersistedMyEntity(myEntity));
    }

    @Test
    @Transactional
    void fullUpdateMyEntityWithPatch() throws Exception {
        // Initialize the database
        insertedMyEntity = myEntityRepository.saveAndFlush(myEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myEntity using partial update
        MyEntity partialUpdatedMyEntity = new MyEntity();
        partialUpdatedMyEntity.setId(myEntity.getId());

        partialUpdatedMyEntity.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMyEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMyEntity))
            )
            .andExpect(status().isOk());

        // Validate the MyEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMyEntityUpdatableFieldsEquals(partialUpdatedMyEntity, getPersistedMyEntity(partialUpdatedMyEntity));
    }

    @Test
    @Transactional
    void patchNonExistingMyEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myEntity.setId(longCount.incrementAndGet());

        // Create the MyEntity
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(myEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, myEntityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(myEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMyEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myEntity.setId(longCount.incrementAndGet());

        // Create the MyEntity
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(myEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(myEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMyEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myEntity.setId(longCount.incrementAndGet());

        // Create the MyEntity
        MyEntityDTO myEntityDTO = myEntityMapper.toDto(myEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyEntityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(myEntityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMyEntity() throws Exception {
        // Initialize the database
        insertedMyEntity = myEntityRepository.saveAndFlush(myEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the myEntity
        restMyEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, myEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return myEntityRepository.count();
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

    protected MyEntity getPersistedMyEntity(MyEntity myEntity) {
        return myEntityRepository.findById(myEntity.getId()).orElseThrow();
    }

    protected void assertPersistedMyEntityToMatchAllProperties(MyEntity expectedMyEntity) {
        assertMyEntityAllPropertiesEquals(expectedMyEntity, getPersistedMyEntity(expectedMyEntity));
    }

    protected void assertPersistedMyEntityToMatchUpdatableProperties(MyEntity expectedMyEntity) {
        assertMyEntityAllUpdatablePropertiesEquals(expectedMyEntity, getPersistedMyEntity(expectedMyEntity));
    }
}
