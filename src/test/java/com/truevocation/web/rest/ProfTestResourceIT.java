package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.ProfTest;
import com.truevocation.repository.ProfTestRepository;
import com.truevocation.service.dto.ProfTestDTO;
import com.truevocation.service.mapper.ProfTestMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProfTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfTestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTION = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prof-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfTestRepository profTestRepository;

    @Autowired
    private ProfTestMapper profTestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfTestMockMvc;

    private ProfTest profTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfTest createEntity(EntityManager em) {
        ProfTest profTest = new ProfTest().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).instruction(DEFAULT_INSTRUCTION);
        return profTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfTest createUpdatedEntity(EntityManager em) {
        ProfTest profTest = new ProfTest().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).instruction(UPDATED_INSTRUCTION);
        return profTest;
    }

    @BeforeEach
    public void initTest() {
        profTest = createEntity(em);
    }

    @Test
    @Transactional
    void createProfTest() throws Exception {
        int databaseSizeBeforeCreate = profTestRepository.findAll().size();
        // Create the ProfTest
        ProfTestDTO profTestDTO = profTestMapper.toDto(profTest);
        restProfTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profTestDTO)))
            .andExpect(status().isCreated());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeCreate + 1);
        ProfTest testProfTest = profTestList.get(profTestList.size() - 1);
        assertThat(testProfTest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProfTest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProfTest.getInstruction()).isEqualTo(DEFAULT_INSTRUCTION);
    }

    @Test
    @Transactional
    void createProfTestWithExistingId() throws Exception {
        // Create the ProfTest with an existing ID
        profTest.setId(1L);
        ProfTestDTO profTestDTO = profTestMapper.toDto(profTest);

        int databaseSizeBeforeCreate = profTestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profTestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProfTests() throws Exception {
        // Initialize the database
        profTestRepository.saveAndFlush(profTest);

        // Get all the profTestList
        restProfTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].instruction").value(hasItem(DEFAULT_INSTRUCTION)));
    }

    @Test
    @Transactional
    void getProfTest() throws Exception {
        // Initialize the database
        profTestRepository.saveAndFlush(profTest);

        // Get the profTest
        restProfTestMockMvc
            .perform(get(ENTITY_API_URL_ID, profTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profTest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.instruction").value(DEFAULT_INSTRUCTION));
    }

    @Test
    @Transactional
    void getNonExistingProfTest() throws Exception {
        // Get the profTest
        restProfTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProfTest() throws Exception {
        // Initialize the database
        profTestRepository.saveAndFlush(profTest);

        int databaseSizeBeforeUpdate = profTestRepository.findAll().size();

        // Update the profTest
        ProfTest updatedProfTest = profTestRepository.findById(profTest.getId()).get();
        // Disconnect from session so that the updates on updatedProfTest are not directly saved in db
        em.detach(updatedProfTest);
        updatedProfTest.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).instruction(UPDATED_INSTRUCTION);
        ProfTestDTO profTestDTO = profTestMapper.toDto(updatedProfTest);

        restProfTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profTestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(profTestDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeUpdate);
        ProfTest testProfTest = profTestList.get(profTestList.size() - 1);
        assertThat(testProfTest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfTest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProfTest.getInstruction()).isEqualTo(UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    void putNonExistingProfTest() throws Exception {
        int databaseSizeBeforeUpdate = profTestRepository.findAll().size();
        profTest.setId(count.incrementAndGet());

        // Create the ProfTest
        ProfTestDTO profTestDTO = profTestMapper.toDto(profTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profTestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(profTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfTest() throws Exception {
        int databaseSizeBeforeUpdate = profTestRepository.findAll().size();
        profTest.setId(count.incrementAndGet());

        // Create the ProfTest
        ProfTestDTO profTestDTO = profTestMapper.toDto(profTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(profTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfTest() throws Exception {
        int databaseSizeBeforeUpdate = profTestRepository.findAll().size();
        profTest.setId(count.incrementAndGet());

        // Create the ProfTest
        ProfTestDTO profTestDTO = profTestMapper.toDto(profTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfTestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profTestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfTestWithPatch() throws Exception {
        // Initialize the database
        profTestRepository.saveAndFlush(profTest);

        int databaseSizeBeforeUpdate = profTestRepository.findAll().size();

        // Update the profTest using partial update
        ProfTest partialUpdatedProfTest = new ProfTest();
        partialUpdatedProfTest.setId(profTest.getId());

        partialUpdatedProfTest.instruction(UPDATED_INSTRUCTION);

        restProfTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfTest))
            )
            .andExpect(status().isOk());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeUpdate);
        ProfTest testProfTest = profTestList.get(profTestList.size() - 1);
        assertThat(testProfTest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProfTest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProfTest.getInstruction()).isEqualTo(UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    void fullUpdateProfTestWithPatch() throws Exception {
        // Initialize the database
        profTestRepository.saveAndFlush(profTest);

        int databaseSizeBeforeUpdate = profTestRepository.findAll().size();

        // Update the profTest using partial update
        ProfTest partialUpdatedProfTest = new ProfTest();
        partialUpdatedProfTest.setId(profTest.getId());

        partialUpdatedProfTest.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).instruction(UPDATED_INSTRUCTION);

        restProfTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfTest))
            )
            .andExpect(status().isOk());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeUpdate);
        ProfTest testProfTest = profTestList.get(profTestList.size() - 1);
        assertThat(testProfTest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfTest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProfTest.getInstruction()).isEqualTo(UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    void patchNonExistingProfTest() throws Exception {
        int databaseSizeBeforeUpdate = profTestRepository.findAll().size();
        profTest.setId(count.incrementAndGet());

        // Create the ProfTest
        ProfTestDTO profTestDTO = profTestMapper.toDto(profTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profTestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(profTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfTest() throws Exception {
        int databaseSizeBeforeUpdate = profTestRepository.findAll().size();
        profTest.setId(count.incrementAndGet());

        // Create the ProfTest
        ProfTestDTO profTestDTO = profTestMapper.toDto(profTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(profTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfTest() throws Exception {
        int databaseSizeBeforeUpdate = profTestRepository.findAll().size();
        profTest.setId(count.incrementAndGet());

        // Create the ProfTest
        ProfTestDTO profTestDTO = profTestMapper.toDto(profTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfTestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(profTestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfTest in the database
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfTest() throws Exception {
        // Initialize the database
        profTestRepository.saveAndFlush(profTest);

        int databaseSizeBeforeDelete = profTestRepository.findAll().size();

        // Delete the profTest
        restProfTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, profTest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProfTest> profTestList = profTestRepository.findAll();
        assertThat(profTestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
