package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.Likes;
import com.truevocation.repository.LikesRepository;
import com.truevocation.service.dto.LikesDTO;
import com.truevocation.service.mapper.LikesMapper;
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
 * Integration tests for the {@link LikesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikesResourceIT {

    private static final String ENTITY_API_URL = "/api/likes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikesMockMvc;

    private Likes likes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Likes createEntity(EntityManager em) {
        Likes likes = new Likes();
        return likes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Likes createUpdatedEntity(EntityManager em) {
        Likes likes = new Likes();
        return likes;
    }

    @BeforeEach
    public void initTest() {
        likes = createEntity(em);
    }

    @Test
    @Transactional
    void createLikes() throws Exception {
        int databaseSizeBeforeCreate = likesRepository.findAll().size();
        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);
        restLikesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likesDTO)))
            .andExpect(status().isCreated());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeCreate + 1);
        Likes testLikes = likesList.get(likesList.size() - 1);
    }

    @Test
    @Transactional
    void createLikesWithExistingId() throws Exception {
        // Create the Likes with an existing ID
        likes.setId(1L);
        LikesDTO likesDTO = likesMapper.toDto(likes);

        int databaseSizeBeforeCreate = likesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLikes() throws Exception {
        // Initialize the database
        likesRepository.saveAndFlush(likes);

        // Get all the likesList
        restLikesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likes.getId().intValue())));
    }

    @Test
    @Transactional
    void getLikes() throws Exception {
        // Initialize the database
        likesRepository.saveAndFlush(likes);

        // Get the likes
        restLikesMockMvc
            .perform(get(ENTITY_API_URL_ID, likes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likes.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingLikes() throws Exception {
        // Get the likes
        restLikesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLikes() throws Exception {
        // Initialize the database
        likesRepository.saveAndFlush(likes);

        int databaseSizeBeforeUpdate = likesRepository.findAll().size();

        // Update the likes
        Likes updatedLikes = likesRepository.findById(likes.getId()).get();
        // Disconnect from session so that the updates on updatedLikes are not directly saved in db
        em.detach(updatedLikes);
        LikesDTO likesDTO = likesMapper.toDto(updatedLikes);

        restLikesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeUpdate);
        Likes testLikes = likesList.get(likesList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingLikes() throws Exception {
        int databaseSizeBeforeUpdate = likesRepository.findAll().size();
        likes.setId(count.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikes() throws Exception {
        int databaseSizeBeforeUpdate = likesRepository.findAll().size();
        likes.setId(count.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikes() throws Exception {
        int databaseSizeBeforeUpdate = likesRepository.findAll().size();
        likes.setId(count.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikesWithPatch() throws Exception {
        // Initialize the database
        likesRepository.saveAndFlush(likes);

        int databaseSizeBeforeUpdate = likesRepository.findAll().size();

        // Update the likes using partial update
        Likes partialUpdatedLikes = new Likes();
        partialUpdatedLikes.setId(likes.getId());

        restLikesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikes))
            )
            .andExpect(status().isOk());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeUpdate);
        Likes testLikes = likesList.get(likesList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateLikesWithPatch() throws Exception {
        // Initialize the database
        likesRepository.saveAndFlush(likes);

        int databaseSizeBeforeUpdate = likesRepository.findAll().size();

        // Update the likes using partial update
        Likes partialUpdatedLikes = new Likes();
        partialUpdatedLikes.setId(likes.getId());

        restLikesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikes))
            )
            .andExpect(status().isOk());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeUpdate);
        Likes testLikes = likesList.get(likesList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingLikes() throws Exception {
        int databaseSizeBeforeUpdate = likesRepository.findAll().size();
        likes.setId(count.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikes() throws Exception {
        int databaseSizeBeforeUpdate = likesRepository.findAll().size();
        likes.setId(count.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikes() throws Exception {
        int databaseSizeBeforeUpdate = likesRepository.findAll().size();
        likes.setId(count.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(likesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Likes in the database
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikes() throws Exception {
        // Initialize the database
        likesRepository.saveAndFlush(likes);

        int databaseSizeBeforeDelete = likesRepository.findAll().size();

        // Delete the likes
        restLikesMockMvc
            .perform(delete(ENTITY_API_URL_ID, likes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Likes> likesList = likesRepository.findAll();
        assertThat(likesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
