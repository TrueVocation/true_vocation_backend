package com.truevocation.web.rest;

import com.truevocation.repository.RecommendationRepository;
import com.truevocation.service.RecommendationService;
import com.truevocation.service.dto.RecommendationDTO;
import com.truevocation.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.truevocation.domain.Recommendation}.
 */
@RestController
@RequestMapping("/api")
public class RecommendationResource {

    private final Logger log = LoggerFactory.getLogger(RecommendationResource.class);

    private static final String ENTITY_NAME = "recommendation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecommendationService recommendationService;

    private final RecommendationRepository recommendationRepository;

    public RecommendationResource(RecommendationService recommendationService, RecommendationRepository recommendationRepository) {
        this.recommendationService = recommendationService;
        this.recommendationRepository = recommendationRepository;
    }

    /**
     * {@code POST  /recommendations} : Create a new recommendation.
     *
     * @param recommendationDTO the recommendationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recommendationDTO, or with status {@code 400 (Bad Request)} if the recommendation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recommendations")
    public ResponseEntity<RecommendationDTO> createRecommendation(@RequestBody RecommendationDTO recommendationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Recommendation : {}", recommendationDTO);
        if (recommendationDTO.getId() != null) {
            throw new BadRequestAlertException("A new recommendation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecommendationDTO result = recommendationService.save(recommendationDTO);
        return ResponseEntity
            .created(new URI("/api/recommendations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recommendations/:id} : Updates an existing recommendation.
     *
     * @param id the id of the recommendationDTO to save.
     * @param recommendationDTO the recommendationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recommendationDTO,
     * or with status {@code 400 (Bad Request)} if the recommendationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recommendationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recommendations/{id}")
    public ResponseEntity<RecommendationDTO> updateRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecommendationDTO recommendationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Recommendation : {}, {}", id, recommendationDTO);
        if (recommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecommendationDTO result = recommendationService.save(recommendationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recommendationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recommendations/:id} : Partial updates given fields of an existing recommendation, field will ignore if it is null
     *
     * @param id the id of the recommendationDTO to save.
     * @param recommendationDTO the recommendationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recommendationDTO,
     * or with status {@code 400 (Bad Request)} if the recommendationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recommendationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recommendationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recommendations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecommendationDTO> partialUpdateRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecommendationDTO recommendationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recommendation partially : {}, {}", id, recommendationDTO);
        if (recommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecommendationDTO> result = recommendationService.partialUpdate(recommendationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recommendationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recommendations} : get all the recommendations.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recommendations in body.
     */
    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendationDTO>> getAllRecommendations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("testresult-is-null".equals(filter)) {
            log.debug("REST request to get all Recommendations where testResult is null");
            return new ResponseEntity<>(recommendationService.findAllWhereTestResultIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Recommendations");
        Page<RecommendationDTO> page = recommendationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recommendations/:id} : get the "id" recommendation.
     *
     * @param id the id of the recommendationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recommendationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recommendations/{id}")
    public ResponseEntity<RecommendationDTO> getRecommendation(@PathVariable Long id) {
        log.debug("REST request to get Recommendation : {}", id);
        Optional<RecommendationDTO> recommendationDTO = recommendationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recommendationDTO);
    }

    /**
     * {@code DELETE  /recommendations/:id} : delete the "id" recommendation.
     *
     * @param id the id of the recommendationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recommendations/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        log.debug("REST request to delete Recommendation : {}", id);
        recommendationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
