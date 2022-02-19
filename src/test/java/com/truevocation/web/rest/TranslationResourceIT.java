package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.Translation;
import com.truevocation.repository.TranslationRepository;
import com.truevocation.service.dto.TranslationDTO;
import com.truevocation.service.mapper.TranslationMapper;
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
 * Integration tests for the {@link TranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TranslationResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EN = "AAAAAAAAAA";
    private static final String UPDATED_EN = "BBBBBBBBBB";

    private static final String DEFAULT_RU = "AAAAAAAAAA";
    private static final String UPDATED_RU = "BBBBBBBBBB";

    private static final String DEFAULT_KK = "AAAAAAAAAA";
    private static final String UPDATED_KK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private TranslationMapper translationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTranslationMockMvc;

    private Translation translation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Translation createEntity(EntityManager em) {
        Translation translation = new Translation().code(DEFAULT_CODE).en(DEFAULT_EN).ru(DEFAULT_RU).kk(DEFAULT_KK);
        return translation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Translation createUpdatedEntity(EntityManager em) {
        Translation translation = new Translation().code(UPDATED_CODE).en(UPDATED_EN).ru(UPDATED_RU).kk(UPDATED_KK);
        return translation;
    }

    @BeforeEach
    public void initTest() {
        translation = createEntity(em);
    }

    @Test
    @Transactional
    void createTranslation() throws Exception {
        int databaseSizeBeforeCreate = translationRepository.findAll().size();
        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);
        restTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeCreate + 1);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTranslation.getEn()).isEqualTo(DEFAULT_EN);
        assertThat(testTranslation.getRu()).isEqualTo(DEFAULT_RU);
        assertThat(testTranslation.getKk()).isEqualTo(DEFAULT_KK);
    }

    @Test
    @Transactional
    void createTranslationWithExistingId() throws Exception {
        // Create the Translation with an existing ID
        translation.setId(1L);
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        int databaseSizeBeforeCreate = translationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTranslations() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList
        restTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(translation.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].en").value(hasItem(DEFAULT_EN)))
            .andExpect(jsonPath("$.[*].ru").value(hasItem(DEFAULT_RU)))
            .andExpect(jsonPath("$.[*].kk").value(hasItem(DEFAULT_KK)));
    }

    @Test
    @Transactional
    void getTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get the translation
        restTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, translation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(translation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.en").value(DEFAULT_EN))
            .andExpect(jsonPath("$.ru").value(DEFAULT_RU))
            .andExpect(jsonPath("$.kk").value(DEFAULT_KK));
    }

    @Test
    @Transactional
    void getNonExistingTranslation() throws Exception {
        // Get the translation
        restTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        int databaseSizeBeforeUpdate = translationRepository.findAll().size();

        // Update the translation
        Translation updatedTranslation = translationRepository.findById(translation.getId()).get();
        // Disconnect from session so that the updates on updatedTranslation are not directly saved in db
        em.detach(updatedTranslation);
        updatedTranslation.code(UPDATED_CODE).en(UPDATED_EN).ru(UPDATED_RU).kk(UPDATED_KK);
        TranslationDTO translationDTO = translationMapper.toDto(updatedTranslation);

        restTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, translationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTranslation.getEn()).isEqualTo(UPDATED_EN);
        assertThat(testTranslation.getRu()).isEqualTo(UPDATED_RU);
        assertThat(testTranslation.getKk()).isEqualTo(UPDATED_KK);
    }

    @Test
    @Transactional
    void putNonExistingTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, translationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(translationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTranslationWithPatch() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        int databaseSizeBeforeUpdate = translationRepository.findAll().size();

        // Update the translation using partial update
        Translation partialUpdatedTranslation = new Translation();
        partialUpdatedTranslation.setId(translation.getId());

        partialUpdatedTranslation.en(UPDATED_EN).ru(UPDATED_RU).kk(UPDATED_KK);

        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTranslation))
            )
            .andExpect(status().isOk());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTranslation.getEn()).isEqualTo(UPDATED_EN);
        assertThat(testTranslation.getRu()).isEqualTo(UPDATED_RU);
        assertThat(testTranslation.getKk()).isEqualTo(UPDATED_KK);
    }

    @Test
    @Transactional
    void fullUpdateTranslationWithPatch() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        int databaseSizeBeforeUpdate = translationRepository.findAll().size();

        // Update the translation using partial update
        Translation partialUpdatedTranslation = new Translation();
        partialUpdatedTranslation.setId(translation.getId());

        partialUpdatedTranslation.code(UPDATED_CODE).en(UPDATED_EN).ru(UPDATED_RU).kk(UPDATED_KK);

        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTranslation))
            )
            .andExpect(status().isOk());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTranslation.getEn()).isEqualTo(UPDATED_EN);
        assertThat(testTranslation.getRu()).isEqualTo(UPDATED_RU);
        assertThat(testTranslation.getKk()).isEqualTo(UPDATED_KK);
    }

    @Test
    @Transactional
    void patchNonExistingTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, translationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        int databaseSizeBeforeDelete = translationRepository.findAll().size();

        // Delete the translation
        restTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, translation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
