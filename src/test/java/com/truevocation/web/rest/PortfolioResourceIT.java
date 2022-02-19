package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.Portfolio;
import com.truevocation.repository.PortfolioRepository;
import com.truevocation.service.PortfolioService;
import com.truevocation.service.dto.PortfolioDTO;
import com.truevocation.service.mapper.PortfolioMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PortfolioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PortfolioResourceIT {

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_HOBBY = "AAAAAAAAAA";
    private static final String UPDATED_HOBBY = "BBBBBBBBBB";

    private static final String DEFAULT_ABOUT_ME = "AAAAAAAAAA";
    private static final String UPDATED_ABOUT_ME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/portfolios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Mock
    private PortfolioRepository portfolioRepositoryMock;

    @Autowired
    private PortfolioMapper portfolioMapper;

    @Mock
    private PortfolioService portfolioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPortfolioMockMvc;

    private Portfolio portfolio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Portfolio createEntity(EntityManager em) {
        Portfolio portfolio = new Portfolio()
            .picture(DEFAULT_PICTURE)
            .gender(DEFAULT_GENDER)
            .hobby(DEFAULT_HOBBY)
            .aboutMe(DEFAULT_ABOUT_ME);
        return portfolio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Portfolio createUpdatedEntity(EntityManager em) {
        Portfolio portfolio = new Portfolio()
            .picture(UPDATED_PICTURE)
            .gender(UPDATED_GENDER)
            .hobby(UPDATED_HOBBY)
            .aboutMe(UPDATED_ABOUT_ME);
        return portfolio;
    }

    @BeforeEach
    public void initTest() {
        portfolio = createEntity(em);
    }

    @Test
    @Transactional
    void createPortfolio() throws Exception {
        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();
        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);
        restPortfolioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isCreated());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate + 1);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testPortfolio.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPortfolio.getHobby()).isEqualTo(DEFAULT_HOBBY);
        assertThat(testPortfolio.getAboutMe()).isEqualTo(DEFAULT_ABOUT_ME);
    }

    @Test
    @Transactional
    void createPortfolioWithExistingId() throws Exception {
        // Create the Portfolio with an existing ID
        portfolio.setId(1L);
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortfolioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPortfolios() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get all the portfolioList
        restPortfolioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].hobby").value(hasItem(DEFAULT_HOBBY)))
            .andExpect(jsonPath("$.[*].aboutMe").value(hasItem(DEFAULT_ABOUT_ME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPortfoliosWithEagerRelationshipsIsEnabled() throws Exception {
        when(portfolioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPortfolioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(portfolioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPortfoliosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(portfolioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPortfolioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(portfolioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get the portfolio
        restPortfolioMockMvc
            .perform(get(ENTITY_API_URL_ID, portfolio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(portfolio.getId().intValue()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.hobby").value(DEFAULT_HOBBY))
            .andExpect(jsonPath("$.aboutMe").value(DEFAULT_ABOUT_ME));
    }

    @Test
    @Transactional
    void getNonExistingPortfolio() throws Exception {
        // Get the portfolio
        restPortfolioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Update the portfolio
        Portfolio updatedPortfolio = portfolioRepository.findById(portfolio.getId()).get();
        // Disconnect from session so that the updates on updatedPortfolio are not directly saved in db
        em.detach(updatedPortfolio);
        updatedPortfolio.picture(UPDATED_PICTURE).gender(UPDATED_GENDER).hobby(UPDATED_HOBBY).aboutMe(UPDATED_ABOUT_ME);
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(updatedPortfolio);

        restPortfolioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, portfolioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(portfolioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testPortfolio.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPortfolio.getHobby()).isEqualTo(UPDATED_HOBBY);
        assertThat(testPortfolio.getAboutMe()).isEqualTo(UPDATED_ABOUT_ME);
    }

    @Test
    @Transactional
    void putNonExistingPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();
        portfolio.setId(count.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortfolioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, portfolioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(portfolioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();
        portfolio.setId(count.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortfolioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(portfolioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();
        portfolio.setId(count.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortfolioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePortfolioWithPatch() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Update the portfolio using partial update
        Portfolio partialUpdatedPortfolio = new Portfolio();
        partialUpdatedPortfolio.setId(portfolio.getId());

        partialUpdatedPortfolio.picture(UPDATED_PICTURE).hobby(UPDATED_HOBBY).aboutMe(UPDATED_ABOUT_ME);

        restPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPortfolio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPortfolio))
            )
            .andExpect(status().isOk());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testPortfolio.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPortfolio.getHobby()).isEqualTo(UPDATED_HOBBY);
        assertThat(testPortfolio.getAboutMe()).isEqualTo(UPDATED_ABOUT_ME);
    }

    @Test
    @Transactional
    void fullUpdatePortfolioWithPatch() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Update the portfolio using partial update
        Portfolio partialUpdatedPortfolio = new Portfolio();
        partialUpdatedPortfolio.setId(portfolio.getId());

        partialUpdatedPortfolio.picture(UPDATED_PICTURE).gender(UPDATED_GENDER).hobby(UPDATED_HOBBY).aboutMe(UPDATED_ABOUT_ME);

        restPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPortfolio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPortfolio))
            )
            .andExpect(status().isOk());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testPortfolio.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPortfolio.getHobby()).isEqualTo(UPDATED_HOBBY);
        assertThat(testPortfolio.getAboutMe()).isEqualTo(UPDATED_ABOUT_ME);
    }

    @Test
    @Transactional
    void patchNonExistingPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();
        portfolio.setId(count.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, portfolioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(portfolioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();
        portfolio.setId(count.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(portfolioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();
        portfolio.setId(count.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(portfolioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        int databaseSizeBeforeDelete = portfolioRepository.findAll().size();

        // Delete the portfolio
        restPortfolioMockMvc
            .perform(delete(ENTITY_API_URL_ID, portfolio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
