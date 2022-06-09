package com.truevocation.web.rest;

import com.truevocation.repository.AnswerUserRepository;
import com.truevocation.service.AnswerUserService;
import com.truevocation.service.dto.AnswerUserDTO;
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

/**
 * REST controller for managing {@link com.truevocation.domain.AnswerUser}.
 */
@RestController
@RequestMapping("/api")
public class AnswerUserResource {

    private final Logger log = LoggerFactory.getLogger(AnswerUserResource.class);

    private static final String ENTITY_NAME = "answerUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnswerUserService answerUserService;

    private final AnswerUserRepository answerUserRepository;

    public AnswerUserResource(AnswerUserService answerUserService, AnswerUserRepository answerUserRepository) {
        this.answerUserService = answerUserService;
        this.answerUserRepository = answerUserRepository;
    }

    /**
     * {@code POST  /answer-users} : Create a new answerUser.
     *
     * @param answerUserDTO the answerUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new answerUserDTO, or with status {@code 400 (Bad Request)} if the answerUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/answer-users")
    public ResponseEntity<AnswerUserDTO> createAnswerUser(@RequestBody AnswerUserDTO answerUserDTO) throws URISyntaxException {
        log.debug("REST request to save AnswerUser : {}", answerUserDTO);
        if (answerUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new answerUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnswerUserDTO result = answerUserService.save(answerUserDTO);
        return ResponseEntity
            .created(new URI("/api/answer-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/answer-users-list")
    public List<AnswerUserDTO> createAnswerUserList(@RequestBody List<AnswerUserDTO> answerUserDTOs) {
        log.debug("REST request to save AnswerUser : {}", answerUserDTOs);
        answerUserDTOs
            .forEach(answerUserDTO -> {
                if (answerUserDTO.getId() != null) {
                    throw new BadRequestAlertException("A new answerUser cannot already have an ID", ENTITY_NAME, "idexists");
                }
            });
        return answerUserService.saveAnswers(answerUserDTOs);
    }

    @PostMapping("/answer-users-check/{id}")
    public boolean checkAnswerUserList(@PathVariable Long id) {
        return answerUserService.checkAnswerUserList(id);
    }

    /**
     * {@code PUT  /answer-users/:id} : Updates an existing answerUser.
     *
     * @param id            the id of the answerUserDTO to save.
     * @param answerUserDTO the answerUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerUserDTO,
     * or with status {@code 400 (Bad Request)} if the answerUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the answerUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/answer-users/{id}")
    public ResponseEntity<AnswerUserDTO> updateAnswerUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnswerUserDTO answerUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AnswerUser : {}, {}", id, answerUserDTO);
        if (answerUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!answerUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnswerUserDTO result = answerUserService.save(answerUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, answerUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /answer-users/:id} : Partial updates given fields of an existing answerUser, field will ignore if it is null
     *
     * @param id            the id of the answerUserDTO to save.
     * @param answerUserDTO the answerUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerUserDTO,
     * or with status {@code 400 (Bad Request)} if the answerUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the answerUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the answerUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/answer-users/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<AnswerUserDTO> partialUpdateAnswerUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnswerUserDTO answerUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnswerUser partially : {}, {}", id, answerUserDTO);
        if (answerUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!answerUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnswerUserDTO> result = answerUserService.partialUpdate(answerUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, answerUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /answer-users} : get all the answerUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of answerUsers in body.
     */
    @GetMapping("/answer-users")
    public ResponseEntity<List<AnswerUserDTO>> getAllAnswerUsers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AnswerUsers");
        Page<AnswerUserDTO> page = answerUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /answer-users/:id} : get the "id" answerUser.
     *
     * @param id the id of the answerUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the answerUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/answer-users/{id}")
    public ResponseEntity<AnswerUserDTO> getAnswerUser(@PathVariable Long id) {
        log.debug("REST request to get AnswerUser : {}", id);
        Optional<AnswerUserDTO> answerUserDTO = answerUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(answerUserDTO);
    }

    /**
     * {@code DELETE  /answer-users/:id} : delete the "id" answerUser.
     *
     * @param id the id of the answerUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/answer-users/{id}")
    public ResponseEntity<Void> deleteAnswerUser(@PathVariable Long id) {
        log.debug("REST request to delete AnswerUser : {}", id);
        answerUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


}
