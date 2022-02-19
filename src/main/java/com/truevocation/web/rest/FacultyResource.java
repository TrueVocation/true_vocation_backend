package com.truevocation.web.rest;

import com.truevocation.repository.FacultyRepository;
import com.truevocation.service.FacultyService;
import com.truevocation.service.dto.FacultyDTO;
import com.truevocation.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.truevocation.domain.Faculty}.
 */
@RestController
@RequestMapping("/api")
public class FacultyResource {

    private final Logger log = LoggerFactory.getLogger(FacultyResource.class);

    private static final String ENTITY_NAME = "faculty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacultyService facultyService;

    private final FacultyRepository facultyRepository;

    public FacultyResource(FacultyService facultyService, FacultyRepository facultyRepository) {
        this.facultyService = facultyService;
        this.facultyRepository = facultyRepository;
    }

    /**
     * {@code POST  /faculties} : Create a new faculty.
     *
     * @param facultyDTO the facultyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facultyDTO, or with status {@code 400 (Bad Request)} if the faculty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/faculties")
    public ResponseEntity<FacultyDTO> createFaculty(@Valid @RequestBody FacultyDTO facultyDTO) throws URISyntaxException {
        log.debug("REST request to save Faculty : {}", facultyDTO);
        if (facultyDTO.getId() != null) {
            throw new BadRequestAlertException("A new faculty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacultyDTO result = facultyService.save(facultyDTO);
        return ResponseEntity
            .created(new URI("/api/faculties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /faculties/:id} : Updates an existing faculty.
     *
     * @param id the id of the facultyDTO to save.
     * @param facultyDTO the facultyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facultyDTO,
     * or with status {@code 400 (Bad Request)} if the facultyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facultyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/faculties/{id}")
    public ResponseEntity<FacultyDTO> updateFaculty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacultyDTO facultyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Faculty : {}, {}", id, facultyDTO);
        if (facultyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facultyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facultyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacultyDTO result = facultyService.save(facultyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facultyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /faculties/:id} : Partial updates given fields of an existing faculty, field will ignore if it is null
     *
     * @param id the id of the facultyDTO to save.
     * @param facultyDTO the facultyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facultyDTO,
     * or with status {@code 400 (Bad Request)} if the facultyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facultyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facultyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/faculties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacultyDTO> partialUpdateFaculty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacultyDTO facultyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Faculty partially : {}, {}", id, facultyDTO);
        if (facultyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facultyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facultyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacultyDTO> result = facultyService.partialUpdate(facultyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facultyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /faculties} : get all the faculties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of faculties in body.
     */
    @GetMapping("/faculties")
    public ResponseEntity<List<FacultyDTO>> getAllFaculties(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Faculties");
        Page<FacultyDTO> page = facultyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /faculties/:id} : get the "id" faculty.
     *
     * @param id the id of the facultyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facultyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/faculties/{id}")
    public ResponseEntity<FacultyDTO> getFaculty(@PathVariable Long id) {
        log.debug("REST request to get Faculty : {}", id);
        Optional<FacultyDTO> facultyDTO = facultyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facultyDTO);
    }

    /**
     * {@code DELETE  /faculties/:id} : delete the "id" faculty.
     *
     * @param id the id of the facultyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/faculties/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        log.debug("REST request to delete Faculty : {}", id);
        facultyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
