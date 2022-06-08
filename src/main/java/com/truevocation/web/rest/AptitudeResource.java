package com.truevocation.web.rest;


import com.truevocation.repository.AptitudeRepository;
import com.truevocation.service.AptitudeService;
import com.truevocation.service.dto.AptitudeDTO;
import com.truevocation.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AptitudeResource {

    private final Logger log = LoggerFactory.getLogger(AptitudeResource.class);

    private static final String ENTITY_NAME = "aptitude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AptitudeService aptitudeService;

    private final AptitudeRepository aptitudeRepository;

    public AptitudeResource(AptitudeService aptitudeService, AptitudeRepository aptitudeRepository) {
        this.aptitudeService = aptitudeService;
        this.aptitudeRepository = aptitudeRepository;
    }


    /**
     * {@code POST  /aptitudes} : Create a new aptitude.
     *
     * @param aptitudeDTO the aptitudeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aptitudeDTO, or with status {@code 400 (Bad Request)} if the aptitude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aptitudes")
    public ResponseEntity<AptitudeDTO> createAptitude(@RequestBody AptitudeDTO aptitudeDTO) throws URISyntaxException {
        log.debug("REST request to save Aptitude : {}", aptitudeDTO);
        if (aptitudeDTO.getId() != null) {
            throw new BadRequestAlertException("A new aptitude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AptitudeDTO result = aptitudeService.save(aptitudeDTO);
        return ResponseEntity
            .created(new URI("/api/aptitudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aptitudes/:id} : Updates an existing aptitude.
     *
     * @param id the id of the aptitudeDTO to save.
     * @param aptitudeDTO the aptitudeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aptitudeDTO,
     * or with status {@code 400 (Bad Request)} if the aptitudeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aptitudeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aptitudes/{id}")
    public ResponseEntity<AptitudeDTO> updateAptitude(@PathVariable(value = "id", required = false) final Long id, @RequestBody AptitudeDTO aptitudeDTO)
        throws URISyntaxException {
        log.debug("REST request to update Aptitude : {}, {}", id, aptitudeDTO);
        if (aptitudeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aptitudeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aptitudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AptitudeDTO result = aptitudeService.save(aptitudeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aptitudeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aptitudes/:id} : Partial updates given fields of an existing aptitude, field will ignore if it is null
     *
     * @param id the id of the aptitudeDTO to save.
     * @param aptitudeDTO the aptitudeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aptitudeDTO,
     * or with status {@code 400 (Bad Request)} if the aptitudeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aptitudeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aptitudeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aptitudes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AptitudeDTO> partialUpdateAptitude(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AptitudeDTO aptitudeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Aptitude partially : {}, {}", id, aptitudeDTO);
        if (aptitudeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aptitudeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aptitudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AptitudeDTO> result = aptitudeService.partialUpdate(aptitudeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aptitudeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /aptitudes} : get all the aptitudes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aptitudes in body.
     */
    @GetMapping("/aptitudes")
    public ResponseEntity<List<AptitudeDTO>> getAllAptitudes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Aptitudes");
        Page<AptitudeDTO> page = aptitudeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aptitudes/:id} : get the "id" aptitude.
     *
     * @param id the id of the aptitudeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aptitudeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aptitudes/{id}")
    public ResponseEntity<AptitudeDTO> getAptitude(@PathVariable Long id) {
        log.debug("REST request to get Aptitude : {}", id);
        Optional<AptitudeDTO> aptitudeDTO = aptitudeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aptitudeDTO);
    }

    /**
     * {@code DELETE  /aptitudes/:id} : delete the "id" aptitude.
     *
     * @param id the id of the aptitudeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aptitudes/{id}")
    public ResponseEntity<Void> deleteAptitude(@PathVariable Long id) {
        log.debug("REST request to delete aptitude : {}", id);
        aptitudeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
