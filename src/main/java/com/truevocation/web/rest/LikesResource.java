package com.truevocation.web.rest;

import com.truevocation.repository.LikesRepository;
import com.truevocation.service.LikesService;
import com.truevocation.service.dto.LikesDTO;
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
 * REST controller for managing {@link com.truevocation.domain.Likes}.
 */
@RestController
@RequestMapping("/api")
public class LikesResource {

    private final Logger log = LoggerFactory.getLogger(LikesResource.class);

    private static final String ENTITY_NAME = "likes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikesService likesService;

    private final LikesRepository likesRepository;

    public LikesResource(LikesService likesService, LikesRepository likesRepository) {
        this.likesService = likesService;
        this.likesRepository = likesRepository;
    }

    /**
     * {@code POST  /likes} : Create a new likes.
     *
     * @param likesDTO the likesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likesDTO, or with status {@code 400 (Bad Request)} if the likes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/likes")
    public ResponseEntity<LikesDTO> createLikes(@RequestBody LikesDTO likesDTO) throws URISyntaxException {
        log.debug("REST request to save Likes : {}", likesDTO);
        if (likesDTO.getId() != null) {
            throw new BadRequestAlertException("A new likes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LikesDTO result = likesService.save(likesDTO);
        return ResponseEntity
            .created(new URI("/api/likes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /likes/:id} : Updates an existing likes.
     *
     * @param id the id of the likesDTO to save.
     * @param likesDTO the likesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likesDTO,
     * or with status {@code 400 (Bad Request)} if the likesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/likes/{id}")
    public ResponseEntity<LikesDTO> updateLikes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikesDTO likesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Likes : {}, {}", id, likesDTO);
        if (likesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LikesDTO result = likesService.save(likesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /likes/:id} : Partial updates given fields of an existing likes, field will ignore if it is null
     *
     * @param id the id of the likesDTO to save.
     * @param likesDTO the likesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likesDTO,
     * or with status {@code 400 (Bad Request)} if the likesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the likesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the likesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/likes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikesDTO> partialUpdateLikes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikesDTO likesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Likes partially : {}, {}", id, likesDTO);
        if (likesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikesDTO> result = likesService.partialUpdate(likesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /likes} : get all the likes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likes in body.
     */
    @GetMapping("/likes")
    public ResponseEntity<List<LikesDTO>> getAllLikes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Likes");
        Page<LikesDTO> page = likesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /likes/:id} : get the "id" likes.
     *
     * @param id the id of the likesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/likes/{id}")
    public ResponseEntity<LikesDTO> getLikes(@PathVariable Long id) {
        log.debug("REST request to get Likes : {}", id);
        Optional<LikesDTO> likesDTO = likesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(likesDTO);
    }

    /**
     * {@code DELETE  /likes/:id} : delete the "id" likes.
     *
     * @param id the id of the likesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/likes/{id}")
    public ResponseEntity<Void> deleteLikes(@PathVariable Long id) {
        log.debug("REST request to delete Likes : {}", id);
        likesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
