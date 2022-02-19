package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.CommentAnswer;
import com.truevocation.repository.CommentAnswerRepository;
import com.truevocation.service.dto.CommentAnswerDTO;
import com.truevocation.service.mapper.CommentAnswerMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CommentAnswerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommentAnswerResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ADDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADDED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/comment-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentAnswerRepository commentAnswerRepository;

    @Autowired
    private CommentAnswerMapper commentAnswerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentAnswerMockMvc;

    private CommentAnswer commentAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentAnswer createEntity(EntityManager em) {
        CommentAnswer commentAnswer = new CommentAnswer().text(DEFAULT_TEXT).addedDate(DEFAULT_ADDED_DATE);
        return commentAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentAnswer createUpdatedEntity(EntityManager em) {
        CommentAnswer commentAnswer = new CommentAnswer().text(UPDATED_TEXT).addedDate(UPDATED_ADDED_DATE);
        return commentAnswer;
    }

    @BeforeEach
    public void initTest() {
        commentAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void createCommentAnswer() throws Exception {
        int databaseSizeBeforeCreate = commentAnswerRepository.findAll().size();
        // Create the CommentAnswer
        CommentAnswerDTO commentAnswerDTO = commentAnswerMapper.toDto(commentAnswer);
        restCommentAnswerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentAnswerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        CommentAnswer testCommentAnswer = commentAnswerList.get(commentAnswerList.size() - 1);
        assertThat(testCommentAnswer.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testCommentAnswer.getAddedDate()).isEqualTo(DEFAULT_ADDED_DATE);
    }

    @Test
    @Transactional
    void createCommentAnswerWithExistingId() throws Exception {
        // Create the CommentAnswer with an existing ID
        commentAnswer.setId(1L);
        CommentAnswerDTO commentAnswerDTO = commentAnswerMapper.toDto(commentAnswer);

        int databaseSizeBeforeCreate = commentAnswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentAnswerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommentAnswers() throws Exception {
        // Initialize the database
        commentAnswerRepository.saveAndFlush(commentAnswer);

        // Get all the commentAnswerList
        restCommentAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCommentAnswer() throws Exception {
        // Initialize the database
        commentAnswerRepository.saveAndFlush(commentAnswer);

        // Get the commentAnswer
        restCommentAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, commentAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentAnswer.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCommentAnswer() throws Exception {
        // Get the commentAnswer
        restCommentAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommentAnswer() throws Exception {
        // Initialize the database
        commentAnswerRepository.saveAndFlush(commentAnswer);

        int databaseSizeBeforeUpdate = commentAnswerRepository.findAll().size();

        // Update the commentAnswer
        CommentAnswer updatedCommentAnswer = commentAnswerRepository.findById(commentAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedCommentAnswer are not directly saved in db
        em.detach(updatedCommentAnswer);
        updatedCommentAnswer.text(UPDATED_TEXT).addedDate(UPDATED_ADDED_DATE);
        CommentAnswerDTO commentAnswerDTO = commentAnswerMapper.toDto(updatedCommentAnswer);

        restCommentAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentAnswerDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeUpdate);
        CommentAnswer testCommentAnswer = commentAnswerList.get(commentAnswerList.size() - 1);
        assertThat(testCommentAnswer.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testCommentAnswer.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCommentAnswer() throws Exception {
        int databaseSizeBeforeUpdate = commentAnswerRepository.findAll().size();
        commentAnswer.setId(count.incrementAndGet());

        // Create the CommentAnswer
        CommentAnswerDTO commentAnswerDTO = commentAnswerMapper.toDto(commentAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentAnswer() throws Exception {
        int databaseSizeBeforeUpdate = commentAnswerRepository.findAll().size();
        commentAnswer.setId(count.incrementAndGet());

        // Create the CommentAnswer
        CommentAnswerDTO commentAnswerDTO = commentAnswerMapper.toDto(commentAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentAnswer() throws Exception {
        int databaseSizeBeforeUpdate = commentAnswerRepository.findAll().size();
        commentAnswer.setId(count.incrementAndGet());

        // Create the CommentAnswer
        CommentAnswerDTO commentAnswerDTO = commentAnswerMapper.toDto(commentAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentAnswerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentAnswerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentAnswerWithPatch() throws Exception {
        // Initialize the database
        commentAnswerRepository.saveAndFlush(commentAnswer);

        int databaseSizeBeforeUpdate = commentAnswerRepository.findAll().size();

        // Update the commentAnswer using partial update
        CommentAnswer partialUpdatedCommentAnswer = new CommentAnswer();
        partialUpdatedCommentAnswer.setId(commentAnswer.getId());

        partialUpdatedCommentAnswer.text(UPDATED_TEXT).addedDate(UPDATED_ADDED_DATE);

        restCommentAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentAnswer))
            )
            .andExpect(status().isOk());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeUpdate);
        CommentAnswer testCommentAnswer = commentAnswerList.get(commentAnswerList.size() - 1);
        assertThat(testCommentAnswer.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testCommentAnswer.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCommentAnswerWithPatch() throws Exception {
        // Initialize the database
        commentAnswerRepository.saveAndFlush(commentAnswer);

        int databaseSizeBeforeUpdate = commentAnswerRepository.findAll().size();

        // Update the commentAnswer using partial update
        CommentAnswer partialUpdatedCommentAnswer = new CommentAnswer();
        partialUpdatedCommentAnswer.setId(commentAnswer.getId());

        partialUpdatedCommentAnswer.text(UPDATED_TEXT).addedDate(UPDATED_ADDED_DATE);

        restCommentAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentAnswer))
            )
            .andExpect(status().isOk());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeUpdate);
        CommentAnswer testCommentAnswer = commentAnswerList.get(commentAnswerList.size() - 1);
        assertThat(testCommentAnswer.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testCommentAnswer.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCommentAnswer() throws Exception {
        int databaseSizeBeforeUpdate = commentAnswerRepository.findAll().size();
        commentAnswer.setId(count.incrementAndGet());

        // Create the CommentAnswer
        CommentAnswerDTO commentAnswerDTO = commentAnswerMapper.toDto(commentAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentAnswerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentAnswer() throws Exception {
        int databaseSizeBeforeUpdate = commentAnswerRepository.findAll().size();
        commentAnswer.setId(count.incrementAndGet());

        // Create the CommentAnswer
        CommentAnswerDTO commentAnswerDTO = commentAnswerMapper.toDto(commentAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentAnswer() throws Exception {
        int databaseSizeBeforeUpdate = commentAnswerRepository.findAll().size();
        commentAnswer.setId(count.incrementAndGet());

        // Create the CommentAnswer
        CommentAnswerDTO commentAnswerDTO = commentAnswerMapper.toDto(commentAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentAnswerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentAnswer in the database
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommentAnswer() throws Exception {
        // Initialize the database
        commentAnswerRepository.saveAndFlush(commentAnswer);

        int databaseSizeBeforeDelete = commentAnswerRepository.findAll().size();

        // Delete the commentAnswer
        restCommentAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentAnswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommentAnswer> commentAnswerList = commentAnswerRepository.findAll();
        assertThat(commentAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
