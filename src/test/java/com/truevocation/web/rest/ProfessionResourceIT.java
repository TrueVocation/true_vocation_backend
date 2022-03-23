package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.Profession;
import com.truevocation.repository.ProfessionRepository;
import com.truevocation.service.ProfessionService;
import com.truevocation.service.dto.ProfessionDTO;
import com.truevocation.service.mapper.ProfessionMapper;
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
 * Integration tests for the {@link ProfessionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYABILITY = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYABILITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_AVERAGE_SALARY = 1;
    private static final Integer UPDATED_AVERAGE_SALARY = 2;

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/professions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfessionRepository professionRepository;

    @Mock
    private ProfessionRepository professionRepositoryMock;

    @Autowired
    private ProfessionMapper professionMapper;

    @Mock
    private ProfessionService professionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionMockMvc;

    private Profession profession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profession createEntity(EntityManager em) {
        Profession profession = new Profession()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .employability(DEFAULT_EMPLOYABILITY)
            .averageSalary(DEFAULT_AVERAGE_SALARY)
            .picture(DEFAULT_PICTURE);
        return profession;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profession createUpdatedEntity(EntityManager em) {
        Profession profession = new Profession()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .employability(UPDATED_EMPLOYABILITY)
            .averageSalary(UPDATED_AVERAGE_SALARY)
            .picture(UPDATED_PICTURE);
        return profession;
    }

    @BeforeEach
    public void initTest() {
        profession = createEntity(em);
    }

    @Test
    @Transactional
    void createProfession() throws Exception {
        int databaseSizeBeforeCreate = professionRepository.findAll().size();
        // Create the Profession
        ProfessionDTO professionDTO = professionMapper.toDto(profession);
        restProfessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionDTO)))
            .andExpect(status().isCreated());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeCreate + 1);
        Profession testProfession = professionList.get(professionList.size() - 1);
        assertThat(testProfession.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProfession.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProfession.getEmployability()).isEqualTo(DEFAULT_EMPLOYABILITY);
        assertThat(testProfession.getAverageSalary()).isEqualTo(DEFAULT_AVERAGE_SALARY);
        assertThat(testProfession.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    void createProfessionWithExistingId() throws Exception {
        // Create the Profession with an existing ID
        profession.setId(1L);
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        int databaseSizeBeforeCreate = professionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProfessions() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get all the professionList
        restProfessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profession.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].employability").value(hasItem(DEFAULT_EMPLOYABILITY)))
            .andExpect(jsonPath("$.[*].averageSalary").value(hasItem(DEFAULT_AVERAGE_SALARY)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProfession() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get the profession
        restProfessionMockMvc
            .perform(get(ENTITY_API_URL_ID, profession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profession.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.employability").value(DEFAULT_EMPLOYABILITY))
            .andExpect(jsonPath("$.averageSalary").value(DEFAULT_AVERAGE_SALARY))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE));
    }

    @Test
    @Transactional
    void getNonExistingProfession() throws Exception {
        // Get the profession
        restProfessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProfession() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        int databaseSizeBeforeUpdate = professionRepository.findAll().size();

        // Update the profession
        Profession updatedProfession = professionRepository.findById(profession.getId()).get();
        // Disconnect from session so that the updates on updatedProfession are not directly saved in db
        em.detach(updatedProfession);
        updatedProfession
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .employability(UPDATED_EMPLOYABILITY)
            .averageSalary(UPDATED_AVERAGE_SALARY)
            .picture(UPDATED_PICTURE);
        ProfessionDTO professionDTO = professionMapper.toDto(updatedProfession);

        restProfessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
        Profession testProfession = professionList.get(professionList.size() - 1);
        assertThat(testProfession.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfession.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProfession.getEmployability()).isEqualTo(UPDATED_EMPLOYABILITY);
        assertThat(testProfession.getAverageSalary()).isEqualTo(UPDATED_AVERAGE_SALARY);
        assertThat(testProfession.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void putNonExistingProfession() throws Exception {
        int databaseSizeBeforeUpdate = professionRepository.findAll().size();
        profession.setId(count.incrementAndGet());

        // Create the Profession
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfession() throws Exception {
        int databaseSizeBeforeUpdate = professionRepository.findAll().size();
        profession.setId(count.incrementAndGet());

        // Create the Profession
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfession() throws Exception {
        int databaseSizeBeforeUpdate = professionRepository.findAll().size();
        profession.setId(count.incrementAndGet());

        // Create the Profession
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionWithPatch() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        int databaseSizeBeforeUpdate = professionRepository.findAll().size();

        // Update the profession using partial update
        Profession partialUpdatedProfession = new Profession();
        partialUpdatedProfession.setId(profession.getId());

        partialUpdatedProfession.name(UPDATED_NAME).employability(UPDATED_EMPLOYABILITY).averageSalary(UPDATED_AVERAGE_SALARY);

        restProfessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfession))
            )
            .andExpect(status().isOk());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
        Profession testProfession = professionList.get(professionList.size() - 1);
        assertThat(testProfession.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfession.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProfession.getEmployability()).isEqualTo(UPDATED_EMPLOYABILITY);
        assertThat(testProfession.getAverageSalary()).isEqualTo(UPDATED_AVERAGE_SALARY);
        assertThat(testProfession.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    void fullUpdateProfessionWithPatch() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        int databaseSizeBeforeUpdate = professionRepository.findAll().size();

        // Update the profession using partial update
        Profession partialUpdatedProfession = new Profession();
        partialUpdatedProfession.setId(profession.getId());

        partialUpdatedProfession
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .employability(UPDATED_EMPLOYABILITY)
            .averageSalary(UPDATED_AVERAGE_SALARY)
            .picture(UPDATED_PICTURE);

        restProfessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfession))
            )
            .andExpect(status().isOk());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
        Profession testProfession = professionList.get(professionList.size() - 1);
        assertThat(testProfession.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfession.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProfession.getEmployability()).isEqualTo(UPDATED_EMPLOYABILITY);
        assertThat(testProfession.getAverageSalary()).isEqualTo(UPDATED_AVERAGE_SALARY);
        assertThat(testProfession.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void patchNonExistingProfession() throws Exception {
        int databaseSizeBeforeUpdate = professionRepository.findAll().size();
        profession.setId(count.incrementAndGet());

        // Create the Profession
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfession() throws Exception {
        int databaseSizeBeforeUpdate = professionRepository.findAll().size();
        profession.setId(count.incrementAndGet());

        // Create the Profession
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfession() throws Exception {
        int databaseSizeBeforeUpdate = professionRepository.findAll().size();
        profession.setId(count.incrementAndGet());

        // Create the Profession
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(professionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfession() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        int databaseSizeBeforeDelete = professionRepository.findAll().size();

        // Delete the profession
        restProfessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, profession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
