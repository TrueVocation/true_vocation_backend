package com.truevocation.web.rest;

import com.truevocation.repository.PicturesRepository;
import com.truevocation.service.PicturesService;
import com.truevocation.service.dto.PicturesDTO;
import com.truevocation.web.rest.errors.BadRequestAlertException;

import java.io.IOException;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.truevocation.domain.Pictures}.
 */
@RestController
@RequestMapping("/api")
public class PicturesResource {

    private final Logger log = LoggerFactory.getLogger(PicturesResource.class);

    private static final String ENTITY_NAME = "pictures";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PicturesService picturesService;

    private final PicturesRepository picturesRepository;

    public PicturesResource(PicturesService picturesService, PicturesRepository picturesRepository) {
        this.picturesService = picturesService;
        this.picturesRepository = picturesRepository;
    }


    @PostMapping(value = "/uploadPicture")
//    @PreAuthorize("hasRole(ROLE_ADMIN)")
    public ResponseEntity<PicturesDTO> uploadPicture(@RequestParam(name = "picture") MultipartFile file,
                                @RequestParam(name = "source_Id")Long sourceId,
                                @RequestParam(name = "source_type")String sourceType){
        PicturesDTO picturesDTO = picturesService.savePicture(file, sourceId, sourceType);
        if(!Objects.isNull(picturesDTO)){
            return ResponseEntity.ok(picturesDTO);
        }
        return ResponseEntity.badRequest().build();
    }



    @GetMapping(value = "/viewPicture",produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public @ResponseBody byte[] viewItemPicture(@RequestParam(name = "url")String url) throws IOException {
        return picturesService.getPictureByUrl(url);
    }


    /**
     * {@code POST  /pictures} : Create a new pictures.
     *
     * @param picturesDTO the picturesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new picturesDTO, or with status {@code 400 (Bad Request)} if the pictures has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pictures")
    public ResponseEntity<PicturesDTO> createPictures(@Valid @RequestBody PicturesDTO picturesDTO) throws URISyntaxException {
        log.debug("REST request to save Pictures : {}", picturesDTO);
        if (picturesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pictures cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PicturesDTO result = picturesService.save(picturesDTO);
        return ResponseEntity
            .created(new URI("/api/pictures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pictures/:id} : Updates an existing pictures.
     *
     * @param id the id of the picturesDTO to save.
     * @param picturesDTO the picturesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated picturesDTO,
     * or with status {@code 400 (Bad Request)} if the picturesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the picturesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pictures/{id}")
    public ResponseEntity<PicturesDTO> updatePictures(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PicturesDTO picturesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pictures : {}, {}", id, picturesDTO);
        if (picturesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, picturesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!picturesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PicturesDTO result = picturesService.save(picturesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, picturesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pictures/:id} : Partial updates given fields of an existing pictures, field will ignore if it is null
     *
     * @param id the id of the picturesDTO to save.
     * @param picturesDTO the picturesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated picturesDTO,
     * or with status {@code 400 (Bad Request)} if the picturesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the picturesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the picturesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pictures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PicturesDTO> partialUpdatePictures(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PicturesDTO picturesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pictures partially : {}, {}", id, picturesDTO);
        if (picturesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, picturesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!picturesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PicturesDTO> result = picturesService.partialUpdate(picturesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, picturesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pictures} : get all the pictures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pictures in body.
     */
    @GetMapping("/pictures")
    public ResponseEntity<List<PicturesDTO>> getAllPictures(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pictures");
        Page<PicturesDTO> page = picturesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pictures/:id} : get the "id" pictures.
     *
     * @param id the id of the picturesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the picturesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pictures/{id}")
    public ResponseEntity<PicturesDTO> getPictures(@PathVariable Long id) {
        log.debug("REST request to get Pictures : {}", id);
        Optional<PicturesDTO> picturesDTO = picturesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(picturesDTO);
    }

    /**
     * {@code DELETE  /pictures/:id} : delete the "id" pictures.
     *
     * @param id the id of the picturesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pictures/{id}")
    public ResponseEntity<Void> deletePictures(@PathVariable Long id) {
        log.debug("REST request to delete Pictures : {}", id);
        picturesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
