package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.Pictures;
import com.truevocation.repository.PicturesRepository;
import com.truevocation.service.dto.PicturesDTO;
import com.truevocation.service.mapper.PicturesMapper;
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
 * Integration tests for the {@link PicturesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PicturesResourceIT {

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pictures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PicturesRepository picturesRepository;

    @Autowired
    private PicturesMapper picturesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPicturesMockMvc;

    private Pictures pictures;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pictures createEntity(EntityManager em) {
        Pictures pictures = new Pictures().picture(DEFAULT_PICTURE);
        return pictures;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pictures createUpdatedEntity(EntityManager em) {
        Pictures pictures = new Pictures().picture(UPDATED_PICTURE);
        return pictures;
    }

    @BeforeEach
    public void initTest() {
        pictures = createEntity(em);
    }

    @Test
    @Transactional
    void createPictures() throws Exception {
        int databaseSizeBeforeCreate = picturesRepository.findAll().size();
        // Create the Pictures
        PicturesDTO picturesDTO = picturesMapper.toDto(pictures);
        restPicturesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picturesDTO)))
            .andExpect(status().isCreated());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeCreate + 1);
        Pictures testPictures = picturesList.get(picturesList.size() - 1);
        assertThat(testPictures.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    void createPicturesWithExistingId() throws Exception {
        // Create the Pictures with an existing ID
        pictures.setId(1L);
        PicturesDTO picturesDTO = picturesMapper.toDto(pictures);

        int databaseSizeBeforeCreate = picturesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPicturesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picturesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPictures() throws Exception {
        // Initialize the database
        picturesRepository.saveAndFlush(pictures);

        // Get all the picturesList
        restPicturesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pictures.getId().intValue())))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    void getPictures() throws Exception {
        // Initialize the database
        picturesRepository.saveAndFlush(pictures);

        // Get the pictures
        restPicturesMockMvc
            .perform(get(ENTITY_API_URL_ID, pictures.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pictures.getId().intValue()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE));
    }

    @Test
    @Transactional
    void getNonExistingPictures() throws Exception {
        // Get the pictures
        restPicturesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPictures() throws Exception {
        // Initialize the database
        picturesRepository.saveAndFlush(pictures);

        int databaseSizeBeforeUpdate = picturesRepository.findAll().size();

        // Update the pictures
        Pictures updatedPictures = picturesRepository.findById(pictures.getId()).get();
        // Disconnect from session so that the updates on updatedPictures are not directly saved in db
        em.detach(updatedPictures);
        updatedPictures.picture(UPDATED_PICTURE);
        PicturesDTO picturesDTO = picturesMapper.toDto(updatedPictures);

        restPicturesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, picturesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(picturesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeUpdate);
        Pictures testPictures = picturesList.get(picturesList.size() - 1);
        assertThat(testPictures.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void putNonExistingPictures() throws Exception {
        int databaseSizeBeforeUpdate = picturesRepository.findAll().size();
        pictures.setId(count.incrementAndGet());

        // Create the Pictures
        PicturesDTO picturesDTO = picturesMapper.toDto(pictures);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPicturesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, picturesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(picturesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPictures() throws Exception {
        int databaseSizeBeforeUpdate = picturesRepository.findAll().size();
        pictures.setId(count.incrementAndGet());

        // Create the Pictures
        PicturesDTO picturesDTO = picturesMapper.toDto(pictures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPicturesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(picturesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPictures() throws Exception {
        int databaseSizeBeforeUpdate = picturesRepository.findAll().size();
        pictures.setId(count.incrementAndGet());

        // Create the Pictures
        PicturesDTO picturesDTO = picturesMapper.toDto(pictures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPicturesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picturesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePicturesWithPatch() throws Exception {
        // Initialize the database
        picturesRepository.saveAndFlush(pictures);

        int databaseSizeBeforeUpdate = picturesRepository.findAll().size();

        // Update the pictures using partial update
        Pictures partialUpdatedPictures = new Pictures();
        partialUpdatedPictures.setId(pictures.getId());

        partialUpdatedPictures.picture(UPDATED_PICTURE);

        restPicturesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPictures.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPictures))
            )
            .andExpect(status().isOk());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeUpdate);
        Pictures testPictures = picturesList.get(picturesList.size() - 1);
        assertThat(testPictures.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void fullUpdatePicturesWithPatch() throws Exception {
        // Initialize the database
        picturesRepository.saveAndFlush(pictures);

        int databaseSizeBeforeUpdate = picturesRepository.findAll().size();

        // Update the pictures using partial update
        Pictures partialUpdatedPictures = new Pictures();
        partialUpdatedPictures.setId(pictures.getId());

        partialUpdatedPictures.picture(UPDATED_PICTURE);

        restPicturesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPictures.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPictures))
            )
            .andExpect(status().isOk());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeUpdate);
        Pictures testPictures = picturesList.get(picturesList.size() - 1);
        assertThat(testPictures.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void patchNonExistingPictures() throws Exception {
        int databaseSizeBeforeUpdate = picturesRepository.findAll().size();
        pictures.setId(count.incrementAndGet());

        // Create the Pictures
        PicturesDTO picturesDTO = picturesMapper.toDto(pictures);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPicturesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, picturesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(picturesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPictures() throws Exception {
        int databaseSizeBeforeUpdate = picturesRepository.findAll().size();
        pictures.setId(count.incrementAndGet());

        // Create the Pictures
        PicturesDTO picturesDTO = picturesMapper.toDto(pictures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPicturesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(picturesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPictures() throws Exception {
        int databaseSizeBeforeUpdate = picturesRepository.findAll().size();
        pictures.setId(count.incrementAndGet());

        // Create the Pictures
        PicturesDTO picturesDTO = picturesMapper.toDto(pictures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPicturesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(picturesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pictures in the database
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePictures() throws Exception {
        // Initialize the database
        picturesRepository.saveAndFlush(pictures);

        int databaseSizeBeforeDelete = picturesRepository.findAll().size();

        // Delete the pictures
        restPicturesMockMvc
            .perform(delete(ENTITY_API_URL_ID, pictures.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pictures> picturesList = picturesRepository.findAll();
        assertThat(picturesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
