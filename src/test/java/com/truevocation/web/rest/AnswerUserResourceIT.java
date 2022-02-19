package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.AnswerUser;
import com.truevocation.repository.AnswerUserRepository;
import com.truevocation.service.dto.AnswerUserDTO;
import com.truevocation.service.mapper.AnswerUserMapper;
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
 * Integration tests for the {@link AnswerUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnswerUserResourceIT {

    private static final String ENTITY_API_URL = "/api/answer-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnswerUserRepository answerUserRepository;

    @Autowired
    private AnswerUserMapper answerUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnswerUserMockMvc;

    private AnswerUser answerUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnswerUser createEntity(EntityManager em) {
        AnswerUser answerUser = new AnswerUser();
        return answerUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnswerUser createUpdatedEntity(EntityManager em) {
        AnswerUser answerUser = new AnswerUser();
        return answerUser;
    }

    @BeforeEach
    public void initTest() {
        answerUser = createEntity(em);
    }

    @Test
    @Transactional
    void createAnswerUser() throws Exception {
        int databaseSizeBeforeCreate = answerUserRepository.findAll().size();
        // Create the AnswerUser
        AnswerUserDTO answerUserDTO = answerUserMapper.toDto(answerUser);
        restAnswerUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerUserDTO)))
            .andExpect(status().isCreated());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeCreate + 1);
        AnswerUser testAnswerUser = answerUserList.get(answerUserList.size() - 1);
    }

    @Test
    @Transactional
    void createAnswerUserWithExistingId() throws Exception {
        // Create the AnswerUser with an existing ID
        answerUser.setId(1L);
        AnswerUserDTO answerUserDTO = answerUserMapper.toDto(answerUser);

        int databaseSizeBeforeCreate = answerUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnswerUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAnswerUsers() throws Exception {
        // Initialize the database
        answerUserRepository.saveAndFlush(answerUser);

        // Get all the answerUserList
        restAnswerUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answerUser.getId().intValue())));
    }

    @Test
    @Transactional
    void getAnswerUser() throws Exception {
        // Initialize the database
        answerUserRepository.saveAndFlush(answerUser);

        // Get the answerUser
        restAnswerUserMockMvc
            .perform(get(ENTITY_API_URL_ID, answerUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(answerUser.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAnswerUser() throws Exception {
        // Get the answerUser
        restAnswerUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnswerUser() throws Exception {
        // Initialize the database
        answerUserRepository.saveAndFlush(answerUser);

        int databaseSizeBeforeUpdate = answerUserRepository.findAll().size();

        // Update the answerUser
        AnswerUser updatedAnswerUser = answerUserRepository.findById(answerUser.getId()).get();
        // Disconnect from session so that the updates on updatedAnswerUser are not directly saved in db
        em.detach(updatedAnswerUser);
        AnswerUserDTO answerUserDTO = answerUserMapper.toDto(updatedAnswerUser);

        restAnswerUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, answerUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(answerUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeUpdate);
        AnswerUser testAnswerUser = answerUserList.get(answerUserList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingAnswerUser() throws Exception {
        int databaseSizeBeforeUpdate = answerUserRepository.findAll().size();
        answerUser.setId(count.incrementAndGet());

        // Create the AnswerUser
        AnswerUserDTO answerUserDTO = answerUserMapper.toDto(answerUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnswerUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, answerUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(answerUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnswerUser() throws Exception {
        int databaseSizeBeforeUpdate = answerUserRepository.findAll().size();
        answerUser.setId(count.incrementAndGet());

        // Create the AnswerUser
        AnswerUserDTO answerUserDTO = answerUserMapper.toDto(answerUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnswerUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(answerUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnswerUser() throws Exception {
        int databaseSizeBeforeUpdate = answerUserRepository.findAll().size();
        answerUser.setId(count.incrementAndGet());

        // Create the AnswerUser
        AnswerUserDTO answerUserDTO = answerUserMapper.toDto(answerUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnswerUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnswerUserWithPatch() throws Exception {
        // Initialize the database
        answerUserRepository.saveAndFlush(answerUser);

        int databaseSizeBeforeUpdate = answerUserRepository.findAll().size();

        // Update the answerUser using partial update
        AnswerUser partialUpdatedAnswerUser = new AnswerUser();
        partialUpdatedAnswerUser.setId(answerUser.getId());

        restAnswerUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnswerUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnswerUser))
            )
            .andExpect(status().isOk());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeUpdate);
        AnswerUser testAnswerUser = answerUserList.get(answerUserList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateAnswerUserWithPatch() throws Exception {
        // Initialize the database
        answerUserRepository.saveAndFlush(answerUser);

        int databaseSizeBeforeUpdate = answerUserRepository.findAll().size();

        // Update the answerUser using partial update
        AnswerUser partialUpdatedAnswerUser = new AnswerUser();
        partialUpdatedAnswerUser.setId(answerUser.getId());

        restAnswerUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnswerUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnswerUser))
            )
            .andExpect(status().isOk());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeUpdate);
        AnswerUser testAnswerUser = answerUserList.get(answerUserList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingAnswerUser() throws Exception {
        int databaseSizeBeforeUpdate = answerUserRepository.findAll().size();
        answerUser.setId(count.incrementAndGet());

        // Create the AnswerUser
        AnswerUserDTO answerUserDTO = answerUserMapper.toDto(answerUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnswerUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, answerUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(answerUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnswerUser() throws Exception {
        int databaseSizeBeforeUpdate = answerUserRepository.findAll().size();
        answerUser.setId(count.incrementAndGet());

        // Create the AnswerUser
        AnswerUserDTO answerUserDTO = answerUserMapper.toDto(answerUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnswerUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(answerUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnswerUser() throws Exception {
        int databaseSizeBeforeUpdate = answerUserRepository.findAll().size();
        answerUser.setId(count.incrementAndGet());

        // Create the AnswerUser
        AnswerUserDTO answerUserDTO = answerUserMapper.toDto(answerUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnswerUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(answerUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnswerUser in the database
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnswerUser() throws Exception {
        // Initialize the database
        answerUserRepository.saveAndFlush(answerUser);

        int databaseSizeBeforeDelete = answerUserRepository.findAll().size();

        // Delete the answerUser
        restAnswerUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, answerUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnswerUser> answerUserList = answerUserRepository.findAll();
        assertThat(answerUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
