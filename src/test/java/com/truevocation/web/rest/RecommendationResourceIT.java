package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.Recommendation;
import com.truevocation.repository.RecommendationRepository;
import com.truevocation.service.dto.RecommendationDTO;
import com.truevocation.service.mapper.RecommendationMapper;
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
 * Integration tests for the {@link RecommendationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecommendationResourceIT {

    private static final String ENTITY_API_URL = "/api/recommendations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RecommendationMapper recommendationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecommendationMockMvc;

    private Recommendation recommendation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recommendation createEntity(EntityManager em) {
        Recommendation recommendation = new Recommendation();
        return recommendation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recommendation createUpdatedEntity(EntityManager em) {
        Recommendation recommendation = new Recommendation();
        return recommendation;
    }

    @BeforeEach
    public void initTest() {
        recommendation = createEntity(em);
    }

    @Test
    @Transactional
    void createRecommendation() throws Exception {
        int databaseSizeBeforeCreate = recommendationRepository.findAll().size();
        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);
        restRecommendationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recommendationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeCreate + 1);
        Recommendation testRecommendation = recommendationList.get(recommendationList.size() - 1);
    }

    @Test
    @Transactional
    void createRecommendationWithExistingId() throws Exception {
        // Create the Recommendation with an existing ID
        recommendation.setId(1L);
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        int databaseSizeBeforeCreate = recommendationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecommendationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecommendations() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get all the recommendationList
        restRecommendationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recommendation.getId().intValue())));
    }

    @Test
    @Transactional
    void getRecommendation() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get the recommendation
        restRecommendationMockMvc
            .perform(get(ENTITY_API_URL_ID, recommendation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recommendation.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRecommendation() throws Exception {
        // Get the recommendation
        restRecommendationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecommendation() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();

        // Update the recommendation
        Recommendation updatedRecommendation = recommendationRepository.findById(recommendation.getId()).get();
        // Disconnect from session so that the updates on updatedRecommendation are not directly saved in db
        em.detach(updatedRecommendation);
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(updatedRecommendation);

        restRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recommendationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recommendationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
        Recommendation testRecommendation = recommendationList.get(recommendationList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingRecommendation() throws Exception {
        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();
        recommendation.setId(count.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recommendationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecommendation() throws Exception {
        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();
        recommendation.setId(count.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecommendation() throws Exception {
        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();
        recommendation.setId(count.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recommendationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecommendationWithPatch() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();

        // Update the recommendation using partial update
        Recommendation partialUpdatedRecommendation = new Recommendation();
        partialUpdatedRecommendation.setId(recommendation.getId());

        restRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecommendation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecommendation))
            )
            .andExpect(status().isOk());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
        Recommendation testRecommendation = recommendationList.get(recommendationList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateRecommendationWithPatch() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();

        // Update the recommendation using partial update
        Recommendation partialUpdatedRecommendation = new Recommendation();
        partialUpdatedRecommendation.setId(recommendation.getId());

        restRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecommendation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecommendation))
            )
            .andExpect(status().isOk());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
        Recommendation testRecommendation = recommendationList.get(recommendationList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingRecommendation() throws Exception {
        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();
        recommendation.setId(count.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recommendationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecommendation() throws Exception {
        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();
        recommendation.setId(count.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecommendation() throws Exception {
        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();
        recommendation.setId(count.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recommendationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecommendation() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        int databaseSizeBeforeDelete = recommendationRepository.findAll().size();

        // Delete the recommendation
        restRecommendationMockMvc
            .perform(delete(ENTITY_API_URL_ID, recommendation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
