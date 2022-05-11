package com.truevocation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.truevocation.IntegrationTest;
import com.truevocation.domain.University;
import com.truevocation.repository.UniversityRepository;
import com.truevocation.service.UniversityService;
import com.truevocation.service.dto.UniversityDTO;
import com.truevocation.service.mapper.UniversityMapper;
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
 * Integration tests for the {@link UniversityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UniversityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DORMITORY = false;
    private static final Boolean UPDATED_DORMITORY = true;

    private static final Boolean DEFAULT_MILITARY = false;
    private static final Boolean UPDATED_MILITARY = true;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/universities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UniversityRepository universityRepository;

    @Mock
    private UniversityRepository universityRepositoryMock;

    @Autowired
    private UniversityMapper universityMapper;

    @Mock
    private UniversityService universityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUniversityMockMvc;

    private University university;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static University createEntity(EntityManager em) {
        University university = new University()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .description(DEFAULT_DESCRIPTION)
            .dormitory(DEFAULT_DORMITORY)
            .military(DEFAULT_MILITARY)
            .status(DEFAULT_STATUS)
            .code(DEFAULT_CODE)
            .logo(DEFAULT_LOGO)
            .shortName(DEFAULT_SHORT_NAME);
        return university;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static University createUpdatedEntity(EntityManager em) {
        University university = new University()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .description(UPDATED_DESCRIPTION)
            .dormitory(UPDATED_DORMITORY)
            .military(UPDATED_MILITARY)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .logo(UPDATED_LOGO)
            .shortName(UPDATED_SHORT_NAME);
        return university;
    }

    @BeforeEach
    public void initTest() {
        university = createEntity(em);
    }

    @Test
    @Transactional
    void createUniversity() throws Exception {
        int databaseSizeBeforeCreate = universityRepository.findAll().size();
        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);
        restUniversityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isCreated());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeCreate + 1);
        University testUniversity = universityList.get(universityList.size() - 1);
        assertThat(testUniversity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUniversity.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testUniversity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUniversity.getDormitory()).isEqualTo(DEFAULT_DORMITORY);
        assertThat(testUniversity.getMilitary()).isEqualTo(DEFAULT_MILITARY);
        assertThat(testUniversity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUniversity.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUniversity.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testUniversity.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
    }

    @Test
    @Transactional
    void createUniversityWithExistingId() throws Exception {
        // Create the University with an existing ID
        university.setId(1L);
        UniversityDTO universityDTO = universityMapper.toDto(university);

        int databaseSizeBeforeCreate = universityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniversityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUniversities() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList
        restUniversityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(university.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dormitory").value(hasItem(DEFAULT_DORMITORY.booleanValue())))
            .andExpect(jsonPath("$.[*].military").value(hasItem(DEFAULT_MILITARY.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUniversitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(universityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUniversityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(universityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUniversitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(universityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUniversityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(universityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get the university
        restUniversityMockMvc
            .perform(get(ENTITY_API_URL_ID, university.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(university.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dormitory").value(DEFAULT_DORMITORY.booleanValue()))
            .andExpect(jsonPath("$.military").value(DEFAULT_MILITARY.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingUniversity() throws Exception {
        // Get the university
        restUniversityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        int databaseSizeBeforeUpdate = universityRepository.findAll().size();

        // Update the university
        University updatedUniversity = universityRepository.findById(university.getId()).get();
        // Disconnect from session so that the updates on updatedUniversity are not directly saved in db
        em.detach(updatedUniversity);
        updatedUniversity
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .description(UPDATED_DESCRIPTION)
            .dormitory(UPDATED_DORMITORY)
            .military(UPDATED_MILITARY)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .logo(UPDATED_LOGO)
            .shortName(UPDATED_SHORT_NAME);
        UniversityDTO universityDTO = universityMapper.toDto(updatedUniversity);

        restUniversityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, universityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universityDTO))
            )
            .andExpect(status().isOk());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
        University testUniversity = universityList.get(universityList.size() - 1);
        assertThat(testUniversity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUniversity.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUniversity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUniversity.getDormitory()).isEqualTo(UPDATED_DORMITORY);
        assertThat(testUniversity.getMilitary()).isEqualTo(UPDATED_MILITARY);
        assertThat(testUniversity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUniversity.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUniversity.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testUniversity.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingUniversity() throws Exception {
        int databaseSizeBeforeUpdate = universityRepository.findAll().size();
        university.setId(count.incrementAndGet());

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniversityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, universityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUniversity() throws Exception {
        int databaseSizeBeforeUpdate = universityRepository.findAll().size();
        university.setId(count.incrementAndGet());

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUniversity() throws Exception {
        int databaseSizeBeforeUpdate = universityRepository.findAll().size();
        university.setId(count.incrementAndGet());

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUniversityWithPatch() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        int databaseSizeBeforeUpdate = universityRepository.findAll().size();

        // Update the university using partial update
        University partialUpdatedUniversity = new University();
        partialUpdatedUniversity.setId(university.getId());

        partialUpdatedUniversity
            .address(UPDATED_ADDRESS)
            .military(UPDATED_MILITARY)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .shortName(UPDATED_SHORT_NAME);

        restUniversityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniversity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniversity))
            )
            .andExpect(status().isOk());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
        University testUniversity = universityList.get(universityList.size() - 1);
        assertThat(testUniversity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUniversity.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUniversity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUniversity.getDormitory()).isEqualTo(DEFAULT_DORMITORY);
        assertThat(testUniversity.getMilitary()).isEqualTo(UPDATED_MILITARY);
        assertThat(testUniversity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUniversity.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUniversity.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testUniversity.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateUniversityWithPatch() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        int databaseSizeBeforeUpdate = universityRepository.findAll().size();

        // Update the university using partial update
        University partialUpdatedUniversity = new University();
        partialUpdatedUniversity.setId(university.getId());

        partialUpdatedUniversity
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .description(UPDATED_DESCRIPTION)
            .dormitory(UPDATED_DORMITORY)
            .military(UPDATED_MILITARY)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .logo(UPDATED_LOGO)
            .shortName(UPDATED_SHORT_NAME);

        restUniversityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniversity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniversity))
            )
            .andExpect(status().isOk());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
        University testUniversity = universityList.get(universityList.size() - 1);
        assertThat(testUniversity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUniversity.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUniversity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUniversity.getDormitory()).isEqualTo(UPDATED_DORMITORY);
        assertThat(testUniversity.getMilitary()).isEqualTo(UPDATED_MILITARY);
        assertThat(testUniversity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUniversity.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUniversity.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testUniversity.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingUniversity() throws Exception {
        int databaseSizeBeforeUpdate = universityRepository.findAll().size();
        university.setId(count.incrementAndGet());

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniversityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, universityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUniversity() throws Exception {
        int databaseSizeBeforeUpdate = universityRepository.findAll().size();
        university.setId(count.incrementAndGet());

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUniversity() throws Exception {
        int databaseSizeBeforeUpdate = universityRepository.findAll().size();
        university.setId(count.incrementAndGet());

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(universityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        int databaseSizeBeforeDelete = universityRepository.findAll().size();

        // Delete the university
        restUniversityMockMvc
            .perform(delete(ENTITY_API_URL_ID, university.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
