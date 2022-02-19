package com.truevocation.web.rest;

import com.truevocation.repository.SchoolRepository;
import com.truevocation.service.SchoolService;
import com.truevocation.service.dto.SchoolDTO;
import com.truevocation.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.truevocation.domain.School}.
 */
@RestController
@RequestMapping("/api")
public class SchoolResource {

    private final Logger log = LoggerFactory.getLogger(SchoolResource.class);

    private static final String ENTITY_NAME = "school";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchoolService schoolService;

    private final SchoolRepository schoolRepository;

    public SchoolResource(SchoolService schoolService, SchoolRepository schoolRepository) {
        this.schoolService = schoolService;
        this.schoolRepository = schoolRepository;
    }

    /**
     * {@code POST  /schools} : Create a new school.
     *
     * @param schoolDTO the schoolDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schoolDTO, or with status {@code 400 (Bad Request)} if the school has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schools")
    public ResponseEntity<SchoolDTO> createSchool(@RequestBody SchoolDTO schoolDTO) throws URISyntaxException {
        log.debug("REST request to save School : {}", schoolDTO);
        if (schoolDTO.getId() != null) {
            throw new BadRequestAlertException("A new school cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchoolDTO result = schoolService.save(schoolDTO);
        return ResponseEntity
            .created(new URI("/api/schools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schools/:id} : Updates an existing school.
     *
     * @param id the id of the schoolDTO to save.
     * @param schoolDTO the schoolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolDTO,
     * or with status {@code 400 (Bad Request)} if the schoolDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schoolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schools/{id}")
    public ResponseEntity<SchoolDTO> updateSchool(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchoolDTO schoolDTO
    ) throws URISyntaxException {
        log.debug("REST request to update School : {}, {}", id, schoolDTO);
        if (schoolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchoolDTO result = schoolService.save(schoolDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /schools/:id} : Partial updates given fields of an existing school, field will ignore if it is null
     *
     * @param id the id of the schoolDTO to save.
     * @param schoolDTO the schoolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolDTO,
     * or with status {@code 400 (Bad Request)} if the schoolDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schoolDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schoolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schools/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SchoolDTO> partialUpdateSchool(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchoolDTO schoolDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update School partially : {}, {}", id, schoolDTO);
        if (schoolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchoolDTO> result = schoolService.partialUpdate(schoolDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /schools} : get all the schools.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schools in body.
     */
    @GetMapping("/schools")
    public ResponseEntity<List<SchoolDTO>> getAllSchools(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Schools");
        Page<SchoolDTO> page = schoolService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /schools/:id} : get the "id" school.
     *
     * @param id the id of the schoolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schoolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schools/{id}")
    public ResponseEntity<SchoolDTO> getSchool(@PathVariable Long id) {
        log.debug("REST request to get School : {}", id);
        Optional<SchoolDTO> schoolDTO = schoolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schoolDTO);
    }

    /**
     * {@code DELETE  /schools/:id} : delete the "id" school.
     *
     * @param id the id of the schoolDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schools/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        log.debug("REST request to delete School : {}", id);
        schoolService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
