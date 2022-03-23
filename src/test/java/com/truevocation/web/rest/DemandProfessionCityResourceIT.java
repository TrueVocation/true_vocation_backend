package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.DemandProfessionCity;
import com.truevocation.repository.DemandProfessionCityRepository;
import com.truevocation.service.dto.DemandProfessionCityDTO;
import com.truevocation.service.mapper.DemandProfessionCityMapper;
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
 * Integration tests for the {@link DemandProfessionCityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemandProfessionCityResourceIT {

    private static final Double DEFAULT_ACTUAL_IN_PERCENT = 1D;
    private static final Double UPDATED_ACTUAL_IN_PERCENT = 2D;

    private static final String ENTITY_API_URL = "/api/demand-profession-cities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandProfessionCityRepository demandProfessionCityRepository;

    @Autowired
    private DemandProfessionCityMapper demandProfessionCityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandProfessionCityMockMvc;

    private DemandProfessionCity demandProfessionCity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandProfessionCity createEntity(EntityManager em) {
        DemandProfessionCity demandProfessionCity = new DemandProfessionCity().actualInPercent(DEFAULT_ACTUAL_IN_PERCENT);
        return demandProfessionCity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandProfessionCity createUpdatedEntity(EntityManager em) {
        DemandProfessionCity demandProfessionCity = new DemandProfessionCity().actualInPercent(UPDATED_ACTUAL_IN_PERCENT);
        return demandProfessionCity;
    }

    @BeforeEach
    public void initTest() {
        demandProfessionCity = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandProfessionCity() throws Exception {
        int databaseSizeBeforeCreate = demandProfessionCityRepository.findAll().size();
        // Create the DemandProfessionCity
        DemandProfessionCityDTO demandProfessionCityDTO = demandProfessionCityMapper.toDto(demandProfessionCity);
        restDemandProfessionCityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandProfessionCityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeCreate + 1);
        DemandProfessionCity testDemandProfessionCity = demandProfessionCityList.get(demandProfessionCityList.size() - 1);
        assertThat(testDemandProfessionCity.getActualInPercent()).isEqualTo(DEFAULT_ACTUAL_IN_PERCENT);
    }

    @Test
    @Transactional
    void createDemandProfessionCityWithExistingId() throws Exception {
        // Create the DemandProfessionCity with an existing ID
        demandProfessionCity.setId(1L);
        DemandProfessionCityDTO demandProfessionCityDTO = demandProfessionCityMapper.toDto(demandProfessionCity);

        int databaseSizeBeforeCreate = demandProfessionCityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandProfessionCityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandProfessionCityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDemandProfessionCities() throws Exception {
        // Initialize the database
        demandProfessionCityRepository.saveAndFlush(demandProfessionCity);

        // Get all the demandProfessionCityList
        restDemandProfessionCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandProfessionCity.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualInPercent").value(hasItem(DEFAULT_ACTUAL_IN_PERCENT.doubleValue())));
    }

    @Test
    @Transactional
    void getDemandProfessionCity() throws Exception {
        // Initialize the database
        demandProfessionCityRepository.saveAndFlush(demandProfessionCity);

        // Get the demandProfessionCity
        restDemandProfessionCityMockMvc
            .perform(get(ENTITY_API_URL_ID, demandProfessionCity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandProfessionCity.getId().intValue()))
            .andExpect(jsonPath("$.actualInPercent").value(DEFAULT_ACTUAL_IN_PERCENT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDemandProfessionCity() throws Exception {
        // Get the demandProfessionCity
        restDemandProfessionCityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandProfessionCity() throws Exception {
        // Initialize the database
        demandProfessionCityRepository.saveAndFlush(demandProfessionCity);

        int databaseSizeBeforeUpdate = demandProfessionCityRepository.findAll().size();

        // Update the demandProfessionCity
        DemandProfessionCity updatedDemandProfessionCity = demandProfessionCityRepository.findById(demandProfessionCity.getId()).get();
        // Disconnect from session so that the updates on updatedDemandProfessionCity are not directly saved in db
        em.detach(updatedDemandProfessionCity);
        updatedDemandProfessionCity.actualInPercent(UPDATED_ACTUAL_IN_PERCENT);
        DemandProfessionCityDTO demandProfessionCityDTO = demandProfessionCityMapper.toDto(updatedDemandProfessionCity);

        restDemandProfessionCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandProfessionCityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandProfessionCityDTO))
            )
            .andExpect(status().isOk());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeUpdate);
        DemandProfessionCity testDemandProfessionCity = demandProfessionCityList.get(demandProfessionCityList.size() - 1);
        assertThat(testDemandProfessionCity.getActualInPercent()).isEqualTo(UPDATED_ACTUAL_IN_PERCENT);
    }

    @Test
    @Transactional
    void putNonExistingDemandProfessionCity() throws Exception {
        int databaseSizeBeforeUpdate = demandProfessionCityRepository.findAll().size();
        demandProfessionCity.setId(count.incrementAndGet());

        // Create the DemandProfessionCity
        DemandProfessionCityDTO demandProfessionCityDTO = demandProfessionCityMapper.toDto(demandProfessionCity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandProfessionCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandProfessionCityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandProfessionCityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandProfessionCity() throws Exception {
        int databaseSizeBeforeUpdate = demandProfessionCityRepository.findAll().size();
        demandProfessionCity.setId(count.incrementAndGet());

        // Create the DemandProfessionCity
        DemandProfessionCityDTO demandProfessionCityDTO = demandProfessionCityMapper.toDto(demandProfessionCity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandProfessionCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandProfessionCityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandProfessionCity() throws Exception {
        int databaseSizeBeforeUpdate = demandProfessionCityRepository.findAll().size();
        demandProfessionCity.setId(count.incrementAndGet());

        // Create the DemandProfessionCity
        DemandProfessionCityDTO demandProfessionCityDTO = demandProfessionCityMapper.toDto(demandProfessionCity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandProfessionCityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandProfessionCityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandProfessionCityWithPatch() throws Exception {
        // Initialize the database
        demandProfessionCityRepository.saveAndFlush(demandProfessionCity);

        int databaseSizeBeforeUpdate = demandProfessionCityRepository.findAll().size();

        // Update the demandProfessionCity using partial update
        DemandProfessionCity partialUpdatedDemandProfessionCity = new DemandProfessionCity();
        partialUpdatedDemandProfessionCity.setId(demandProfessionCity.getId());

        partialUpdatedDemandProfessionCity.actualInPercent(UPDATED_ACTUAL_IN_PERCENT);

        restDemandProfessionCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandProfessionCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandProfessionCity))
            )
            .andExpect(status().isOk());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeUpdate);
        DemandProfessionCity testDemandProfessionCity = demandProfessionCityList.get(demandProfessionCityList.size() - 1);
        assertThat(testDemandProfessionCity.getActualInPercent()).isEqualTo(UPDATED_ACTUAL_IN_PERCENT);
    }

    @Test
    @Transactional
    void fullUpdateDemandProfessionCityWithPatch() throws Exception {
        // Initialize the database
        demandProfessionCityRepository.saveAndFlush(demandProfessionCity);

        int databaseSizeBeforeUpdate = demandProfessionCityRepository.findAll().size();

        // Update the demandProfessionCity using partial update
        DemandProfessionCity partialUpdatedDemandProfessionCity = new DemandProfessionCity();
        partialUpdatedDemandProfessionCity.setId(demandProfessionCity.getId());

        partialUpdatedDemandProfessionCity.actualInPercent(UPDATED_ACTUAL_IN_PERCENT);

        restDemandProfessionCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandProfessionCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandProfessionCity))
            )
            .andExpect(status().isOk());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeUpdate);
        DemandProfessionCity testDemandProfessionCity = demandProfessionCityList.get(demandProfessionCityList.size() - 1);
        assertThat(testDemandProfessionCity.getActualInPercent()).isEqualTo(UPDATED_ACTUAL_IN_PERCENT);
    }

    @Test
    @Transactional
    void patchNonExistingDemandProfessionCity() throws Exception {
        int databaseSizeBeforeUpdate = demandProfessionCityRepository.findAll().size();
        demandProfessionCity.setId(count.incrementAndGet());

        // Create the DemandProfessionCity
        DemandProfessionCityDTO demandProfessionCityDTO = demandProfessionCityMapper.toDto(demandProfessionCity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandProfessionCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandProfessionCityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandProfessionCityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandProfessionCity() throws Exception {
        int databaseSizeBeforeUpdate = demandProfessionCityRepository.findAll().size();
        demandProfessionCity.setId(count.incrementAndGet());

        // Create the DemandProfessionCity
        DemandProfessionCityDTO demandProfessionCityDTO = demandProfessionCityMapper.toDto(demandProfessionCity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandProfessionCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandProfessionCityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandProfessionCity() throws Exception {
        int databaseSizeBeforeUpdate = demandProfessionCityRepository.findAll().size();
        demandProfessionCity.setId(count.incrementAndGet());

        // Create the DemandProfessionCity
        DemandProfessionCityDTO demandProfessionCityDTO = demandProfessionCityMapper.toDto(demandProfessionCity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandProfessionCityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandProfessionCityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandProfessionCity in the database
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandProfessionCity() throws Exception {
        // Initialize the database
        demandProfessionCityRepository.saveAndFlush(demandProfessionCity);

        int databaseSizeBeforeDelete = demandProfessionCityRepository.findAll().size();

        // Delete the demandProfessionCity
        restDemandProfessionCityMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandProfessionCity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandProfessionCity> demandProfessionCityList = demandProfessionCityRepository.findAll();
        assertThat(demandProfessionCityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
