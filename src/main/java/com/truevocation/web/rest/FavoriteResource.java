package com.truevocation.web.rest;

import com.truevocation.repository.FavoriteRepository;
import com.truevocation.service.FavoriteService;
import com.truevocation.service.UserService;
import com.truevocation.service.dto.*;
import com.truevocation.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing {@link com.truevocation.domain.Favorite}.
 */
@RestController
@RequestMapping("/api")
public class FavoriteResource {

    private final Logger log = LoggerFactory.getLogger(FavoriteResource.class);

    private static final String ENTITY_NAME = "favorite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavoriteService favoriteService;

    private final FavoriteRepository favoriteRepository;


    @Autowired
    private UserService userService;

    public FavoriteResource(FavoriteService favoriteService, FavoriteRepository favoriteRepository) {
        this.favoriteService = favoriteService;
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * {@code POST  /favorites} : Create a new favorite.
     *
     * @param favoriteDTO the favoriteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new favoriteDTO, or with status {@code 400 (Bad Request)} if the favorite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/favorites")
    public ResponseEntity<FavoriteDTO> createFavorite(@RequestBody FavoriteDTO favoriteDTO) throws URISyntaxException {
        log.debug("REST request to save Favorite : {}", favoriteDTO);
        if (favoriteDTO.getId() != null) {
            throw new BadRequestAlertException("A new favorite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavoriteDTO result = favoriteService.save(favoriteDTO);
        return ResponseEntity
            .created(new URI("/api/favorites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/check-favorites-university")
    public boolean checkFavoritesUniversity(@RequestBody FavoriteDTO favoriteDTO) {
        return favoriteService.isFavoriteUniversity(favoriteDTO.getUniversity().getId(), favoriteDTO.getUser().getId());
    }

    @PostMapping("/check-favorites-speciality")
    public boolean checkFavoritesSpeciality(@RequestBody FavoriteDTO favoriteDTO) {
        return favoriteService.isFavoriteSpeciality(favoriteDTO.getSpecialty().getId(), favoriteDTO.getUser().getId());
    }

    @PostMapping("/check-favorites-profession")
    public boolean checkFavoritesProfession(@RequestBody FavoriteDTO favoriteDTO) {
        return favoriteService.isFavoriteProfession(favoriteDTO.getProfession().getId(), favoriteDTO.getUser().getId());
    }

    @DeleteMapping("/delete-favorites-profession")
    public void deleteFavoritesProfession(@RequestBody FavoriteDTO favoriteDTO) {
        favoriteService.deleteFavoriteProfession(favoriteDTO.getProfession().getId(), favoriteDTO.getUser().getId());
    }

    @DeleteMapping("/delete-favorites-speciality")
    public void deleteFavoritesSpeciality(@RequestBody FavoriteDTO favoriteDTO) {
        favoriteService.deleteFavoriteSpeciality(favoriteDTO.getSpecialty().getId(), favoriteDTO.getUser().getId());
    }

    @DeleteMapping("/delete-favorites-university")
    public void deleteFavoritesUniversity(@RequestBody FavoriteDTO favoriteDTO) {
        favoriteService.deleteFavoriteUniversity(favoriteDTO.getUniversity().getId(), favoriteDTO.getUser().getId());
    }

    @GetMapping("/favorites-universities/{id}")
    public ResponseEntity<List<UniversityDTO>> getAllFavoritesUniversitiesByUser(@org.springdoc.api.annotations.ParameterObject Pageable pageable, @PathVariable Long id) {
        log.debug("REST request to get a page of Favorites-Universities");
        Page<UniversityDTO> page = favoriteService.findAllFavoritesUniversitiesByUserId(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/favorites-specialities/{id}")
    public ResponseEntity<List<SpecialtyDTO>> getAllFavoritesSpecialitiesByUser(@org.springdoc.api.annotations.ParameterObject Pageable pageable, @PathVariable Long id) {
        log.debug("REST request to get a page of Favorites-Universities");
        Page<SpecialtyDTO> page = favoriteService.findAllFavoriteSpecialtyByUserId(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/favorites-professions/{id}")
    public ResponseEntity<List<ProfessionDTO>> getAllFavoritesProfessionsByUser(@org.springdoc.api.annotations.ParameterObject Pageable pageable, @PathVariable Long id) {
        log.debug("REST request to get a page of Favorites-Universities");
        Page<ProfessionDTO> page = favoriteService.findAllFavoriteProfessionByUserId(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/favorites-posts/{id}")
    public ResponseEntity<List<PostDTO>> getAllFavoritesPostsByUser(@org.springdoc.api.annotations.ParameterObject Pageable pageable, @PathVariable Long id) {
        log.debug("REST request to get a page of Favorites-Universities");
        Page<PostDTO> page = favoriteService.findAllFavoritePostByUserId(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code PUT  /favorites/:id} : Updates an existing favorite.
     *
     * @param id          the id of the favoriteDTO to save.
     * @param favoriteDTO the favoriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoriteDTO,
     * or with status {@code 400 (Bad Request)} if the favoriteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the favoriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/favorites/{id}")
    public ResponseEntity<FavoriteDTO> updateFavorite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FavoriteDTO favoriteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Favorite : {}, {}", id, favoriteDTO);
        if (favoriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favoriteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FavoriteDTO result = favoriteService.save(favoriteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favoriteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /favorites/:id} : Partial updates given fields of an existing favorite, field will ignore if it is null
     *
     * @param id          the id of the favoriteDTO to save.
     * @param favoriteDTO the favoriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoriteDTO,
     * or with status {@code 400 (Bad Request)} if the favoriteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the favoriteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the favoriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/favorites/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<FavoriteDTO> partialUpdateFavorite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FavoriteDTO favoriteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Favorite partially : {}, {}", id, favoriteDTO);
        if (favoriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favoriteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FavoriteDTO> result = favoriteService.partialUpdate(favoriteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favoriteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /favorites} : get all the favorites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of favorites in body.
     */
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteDTO>> getAllFavorites(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Favorites");
        Page<FavoriteDTO> page = favoriteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /favorites/:id} : get the "id" favorite.
     *
     * @param id the id of the favoriteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the favoriteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/favorites/{id}")
    public ResponseEntity<FavoriteDTO> getFavorite(@PathVariable Long id) {
        log.debug("REST request to get Favorite : {}", id);
        Optional<FavoriteDTO> favoriteDTO = favoriteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favoriteDTO);
    }

    /**
     * {@code DELETE  /favorites/:id} : delete the "id" favorite.
     *
     * @param id the id of the favoriteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        log.debug("REST request to delete Favorite : {}", id);
        favoriteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/user-favorite")
    public ResponseEntity<Void> setPostFavorite(@RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
        return favoriteService.setPostFavorite(userId, postId);
    }
}
