package com.truevocation.web.rest;

import com.truevocation.repository.TestResultRepository;
import com.truevocation.service.TestResultService;
import com.truevocation.service.dto.TestResultDTO;
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
 * REST controller for managing {@link com.truevocation.domain.TestResult}.
 */
@RestController
@RequestMapping("/api")
public class TestResultResource {

    private final Logger log = LoggerFactory.getLogger(TestResultResource.class);

    private static final String ENTITY_NAME = "testResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestResultService testResultService;

    private final TestResultRepository testResultRepository;

    public TestResultResource(TestResultService testResultService, TestResultRepository testResultRepository) {
        this.testResultService = testResultService;
        this.testResultRepository = testResultRepository;
    }

    /**
     * {@code POST  /test-results} : Create a new testResult.
     *
     * @param testResultDTO the testResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testResultDTO, or with status {@code 400 (Bad Request)} if the testResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-results")
    public ResponseEntity<TestResultDTO> createTestResult(@RequestBody TestResultDTO testResultDTO) throws URISyntaxException {
        log.debug("REST request to save TestResult : {}", testResultDTO);
        if (testResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new testResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestResultDTO result = testResultService.save(testResultDTO);
        return ResponseEntity
            .created(new URI("/api/test-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-results/:id} : Updates an existing testResult.
     *
     * @param id the id of the testResultDTO to save.
     * @param testResultDTO the testResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testResultDTO,
     * or with status {@code 400 (Bad Request)} if the testResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-results/{id}")
    public ResponseEntity<TestResultDTO> updateTestResult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestResultDTO testResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TestResult : {}, {}", id, testResultDTO);
        if (testResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestResultDTO result = testResultService.save(testResultDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-results/:id} : Partial updates given fields of an existing testResult, field will ignore if it is null
     *
     * @param id the id of the testResultDTO to save.
     * @param testResultDTO the testResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testResultDTO,
     * or with status {@code 400 (Bad Request)} if the testResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-results/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestResultDTO> partialUpdateTestResult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestResultDTO testResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestResult partially : {}, {}", id, testResultDTO);
        if (testResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestResultDTO> result = testResultService.partialUpdate(testResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /test-results} : get all the testResults.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testResults in body.
     */
    @GetMapping("/test-results")
    public ResponseEntity<List<TestResultDTO>> getAllTestResults(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TestResults");
        Page<TestResultDTO> page = testResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-results/:id} : get the "id" testResult.
     *
     * @param id the id of the testResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-results/{id}")
    public ResponseEntity<TestResultDTO> getTestResult(@PathVariable Long id) {
        log.debug("REST request to get TestResult : {}", id);
        Optional<TestResultDTO> testResultDTO = testResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testResultDTO);
    }

    /**
     * {@code DELETE  /test-results/:id} : delete the "id" testResult.
     *
     * @param id the id of the testResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-results/{id}")
    public ResponseEntity<Void> deleteTestResult(@PathVariable Long id) {
        log.debug("REST request to delete TestResult : {}", id);
        testResultService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
