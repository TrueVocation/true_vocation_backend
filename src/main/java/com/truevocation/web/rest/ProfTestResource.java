package com.truevocation.web.rest;

import com.truevocation.repository.ProfTestRepository;
import com.truevocation.service.ProfTestService;
import com.truevocation.service.dto.ProfTestDTO;
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
 * REST controller for managing {@link com.truevocation.domain.ProfTest}.
 */
@RestController
@RequestMapping("/api")
public class ProfTestResource {

    private final Logger log = LoggerFactory.getLogger(ProfTestResource.class);

    private static final String ENTITY_NAME = "profTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfTestService profTestService;

    private final ProfTestRepository profTestRepository;

    public ProfTestResource(ProfTestService profTestService, ProfTestRepository profTestRepository) {
        this.profTestService = profTestService;
        this.profTestRepository = profTestRepository;
    }

    /**
     * {@code POST  /prof-tests} : Create a new profTest.
     *
     * @param profTestDTO the profTestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profTestDTO, or with status {@code 400 (Bad Request)} if the profTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prof-tests")
    public ResponseEntity<ProfTestDTO> createProfTest(@Valid @RequestBody ProfTestDTO profTestDTO) throws URISyntaxException {
        log.debug("REST request to save ProfTest : {}", profTestDTO);
        if (profTestDTO.getId() != null) {
            throw new BadRequestAlertException("A new profTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfTestDTO result = profTestService.save(profTestDTO);
        return ResponseEntity
            .created(new URI("/api/prof-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prof-tests/:id} : Updates an existing profTest.
     *
     * @param id the id of the profTestDTO to save.
     * @param profTestDTO the profTestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profTestDTO,
     * or with status {@code 400 (Bad Request)} if the profTestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profTestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prof-tests/{id}")
    public ResponseEntity<ProfTestDTO> updateProfTest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfTestDTO profTestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProfTest : {}, {}", id, profTestDTO);
        if (profTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profTestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProfTestDTO result = profTestService.save(profTestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profTestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prof-tests/:id} : Partial updates given fields of an existing profTest, field will ignore if it is null
     *
     * @param id the id of the profTestDTO to save.
     * @param profTestDTO the profTestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profTestDTO,
     * or with status {@code 400 (Bad Request)} if the profTestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the profTestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the profTestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prof-tests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfTestDTO> partialUpdateProfTest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfTestDTO profTestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProfTest partially : {}, {}", id, profTestDTO);
        if (profTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profTestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfTestDTO> result = profTestService.partialUpdate(profTestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profTestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prof-tests} : get all the profTests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profTests in body.
     */
    @GetMapping("/prof-tests")
    public ResponseEntity<List<ProfTestDTO>> getAllProfTests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProfTests");
        Page<ProfTestDTO> page = profTestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prof-tests/:id} : get the "id" profTest.
     *
     * @param id the id of the profTestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profTestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prof-tests/{id}")
    public ResponseEntity<ProfTestDTO> getProfTest(@PathVariable Long id) {
        log.debug("REST request to get ProfTest : {}", id);
        Optional<ProfTestDTO> profTestDTO = profTestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profTestDTO);
    }

    /**
     * {@code DELETE  /prof-tests/:id} : delete the "id" profTest.
     *
     * @param id the id of the profTestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prof-tests/{id}")
    public ResponseEntity<Void> deleteProfTest(@PathVariable Long id) {
        log.debug("REST request to delete ProfTest : {}", id);
        profTestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
