package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.TestResult;
import com.truevocation.repository.TestResultRepository;
import com.truevocation.service.dto.TestResultDTO;
import com.truevocation.service.mapper.TestResultMapper;
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
 * Integration tests for the {@link TestResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestResultResourceIT {

    private static final String ENTITY_API_URL = "/api/test-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TestResultMapper testResultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestResultMockMvc;

    private TestResult testResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestResult createEntity(EntityManager em) {
        TestResult testResult = new TestResult();
        return testResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestResult createUpdatedEntity(EntityManager em) {
        TestResult testResult = new TestResult();
        return testResult;
    }

    @BeforeEach
    public void initTest() {
        testResult = createEntity(em);
    }

    @Test
    @Transactional
    void createTestResult() throws Exception {
        int databaseSizeBeforeCreate = testResultRepository.findAll().size();
        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);
        restTestResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testResultDTO)))
            .andExpect(status().isCreated());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeCreate + 1);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
    }

    @Test
    @Transactional
    void createTestResultWithExistingId() throws Exception {
        // Create the TestResult with an existing ID
        testResult.setId(1L);
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        int databaseSizeBeforeCreate = testResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestResults() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList
        restTestResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testResult.getId().intValue())));
    }

    @Test
    @Transactional
    void getTestResult() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get the testResult
        restTestResultMockMvc
            .perform(get(ENTITY_API_URL_ID, testResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testResult.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestResult() throws Exception {
        // Get the testResult
        restTestResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTestResult() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();

        // Update the testResult
        TestResult updatedTestResult = testResultRepository.findById(testResult.getId()).get();
        // Disconnect from session so that the updates on updatedTestResult are not directly saved in db
        em.detach(updatedTestResult);
        TestResultDTO testResultDTO = testResultMapper.toDto(updatedTestResult);

        restTestResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestResultWithPatch() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();

        // Update the testResult using partial update
        TestResult partialUpdatedTestResult = new TestResult();
        partialUpdatedTestResult.setId(testResult.getId());

        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestResult))
            )
            .andExpect(status().isOk());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateTestResultWithPatch() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();

        // Update the testResult using partial update
        TestResult partialUpdatedTestResult = new TestResult();
        partialUpdatedTestResult.setId(testResult.getId());

        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestResult))
            )
            .andExpect(status().isOk());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestResult() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        int databaseSizeBeforeDelete = testResultRepository.findAll().size();

        // Delete the testResult
        restTestResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, testResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
