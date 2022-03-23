package com.truevocation.web.rest;

import com.truevocation.repository.CommentAnswerRepository;
import com.truevocation.service.CommentAnswerService;
import com.truevocation.service.dto.CommentAnswerDTO;
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
 * REST controller for managing {@link com.truevocation.domain.CommentAnswer}.
 */
@RestController
@RequestMapping("/api")
public class CommentAnswerResource {

    private final Logger log = LoggerFactory.getLogger(CommentAnswerResource.class);

    private static final String ENTITY_NAME = "commentAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentAnswerService commentAnswerService;

    private final CommentAnswerRepository commentAnswerRepository;

    public CommentAnswerResource(CommentAnswerService commentAnswerService, CommentAnswerRepository commentAnswerRepository) {
        this.commentAnswerService = commentAnswerService;
        this.commentAnswerRepository = commentAnswerRepository;
    }

    /**
     * {@code POST  /comment-answers} : Create a new commentAnswer.
     *
     * @param commentAnswerDTO the commentAnswerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentAnswerDTO, or with status {@code 400 (Bad Request)} if the commentAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comment-answers")
    public ResponseEntity<CommentAnswerDTO> createCommentAnswer(@RequestBody CommentAnswerDTO commentAnswerDTO) throws URISyntaxException {
        log.debug("REST request to save CommentAnswer : {}", commentAnswerDTO);
        if (commentAnswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentAnswerDTO result = commentAnswerService.save(commentAnswerDTO);
        return ResponseEntity
            .created(new URI("/api/comment-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comment-answers/:id} : Updates an existing commentAnswer.
     *
     * @param id the id of the commentAnswerDTO to save.
     * @param commentAnswerDTO the commentAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the commentAnswerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comment-answers/{id}")
    public ResponseEntity<CommentAnswerDTO> updateCommentAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommentAnswerDTO commentAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommentAnswer : {}, {}", id, commentAnswerDTO);
        if (commentAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommentAnswerDTO result = commentAnswerService.save(commentAnswerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentAnswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comment-answers/:id} : Partial updates given fields of an existing commentAnswer, field will ignore if it is null
     *
     * @param id the id of the commentAnswerDTO to save.
     * @param commentAnswerDTO the commentAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the commentAnswerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commentAnswerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commentAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comment-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommentAnswerDTO> partialUpdateCommentAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommentAnswerDTO commentAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommentAnswer partially : {}, {}", id, commentAnswerDTO);
        if (commentAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommentAnswerDTO> result = commentAnswerService.partialUpdate(commentAnswerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentAnswerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /comment-answers} : get all the commentAnswers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentAnswers in body.
     */
    @GetMapping("/comment-answers")
    public ResponseEntity<List<CommentAnswerDTO>> getAllCommentAnswers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CommentAnswers");
        Page<CommentAnswerDTO> page = commentAnswerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comment-answers/:id} : get the "id" commentAnswer.
     *
     * @param id the id of the commentAnswerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentAnswerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comment-answers/{id}")
    public ResponseEntity<CommentAnswerDTO> getCommentAnswer(@PathVariable Long id) {
        log.debug("REST request to get CommentAnswer : {}", id);
        Optional<CommentAnswerDTO> commentAnswerDTO = commentAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentAnswerDTO);
    }

    /**
     * {@code DELETE  /comment-answers/:id} : delete the "id" commentAnswer.
     *
     * @param id the id of the commentAnswerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comment-answers/{id}")
    public ResponseEntity<Void> deleteCommentAnswer(@PathVariable Long id) {
        log.debug("REST request to delete CommentAnswer : {}", id);
        commentAnswerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
