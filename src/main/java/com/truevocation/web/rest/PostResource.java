package com.truevocation.web.rest;

import com.truevocation.repository.PostRepository;
import com.truevocation.service.PostService;
import com.truevocation.service.dto.PostDTO;
import com.truevocation.service.dto.PostsPageDTO;
import com.truevocation.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.truevocation.domain.Post}.
 */
@RestController
@RequestMapping("/api")
public class PostResource {

    private final Logger log = LoggerFactory.getLogger(PostResource.class);

    private static final String ENTITY_NAME = "post";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostService postService;

    private final PostRepository postRepository;

    public PostResource(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    /**
     * {@code POST  /posts} : Create a new post.
     *
     * @param postDTO the postDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postDTO, or with status {@code 400 (Bad Request)} if the post has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/posts")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) throws URISyntaxException {
        log.debug("REST request to save Post : {}", postDTO);
        if (postDTO.getId() != null) {
            throw new BadRequestAlertException("A new post cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostDTO result = postService.save(postDTO);
        return ResponseEntity
            .created(new URI("/api/posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /posts/:id} : Updates an existing post.
     *
     * @param id      the id of the postDTO to save.
     * @param postDTO the postDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postDTO,
     * or with status {@code 400 (Bad Request)} if the postDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostDTO> updatePost(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostDTO postDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Post : {}, {}", id, postDTO);
        if (postDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostDTO result = postService.save(postDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /posts/:id} : Partial updates given fields of an existing post, field will ignore if it is null
     *
     * @param id      the id of the postDTO to save.
     * @param postDTO the postDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postDTO,
     * or with status {@code 400 (Bad Request)} if the postDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/posts/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<PostDTO> partialUpdatePost(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostDTO postDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Post partially : {}, {}", id, postDTO);
        if (postDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostDTO> result = postService.partialUpdate(postDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posts in body.
     */
    @GetMapping("/posts")
    public ResponseEntity<Page<PostDTO>> getAllPosts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Posts");
        Page<PostDTO> page = postService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/posts-list")
    public ResponseEntity<List<PostDTO>> getAllPostsList(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Posts");
        Page<PostDTO> page = postService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/posts-today")
    public ResponseEntity<List<PostDTO>> getTodayPosts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Posts");

        Page<PostDTO> page = postService.findAllToday(pageable, LocalDate.now());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/posts-page")
    public ResponseEntity<PostsPageDTO> getAllPostsByQuery(@org.springdoc.api.annotations.ParameterObject Pageable pageable,
                                                           @RequestParam(value = "searchText", defaultValue = "") String searchText) {
        log.debug("REST request to get a page of Posts");
        PostsPageDTO page = postService.findAllForPostsPage(searchText, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page.getLatestPosts());
        return ResponseEntity.ok().headers(headers).body(page);
    }

    /**
     * {@code GET  /posts/:id} : get the "id" post.
     *
     * @param id the id of the postDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        log.debug("REST request to get Post : {}", id);
        Optional<PostDTO> postDTO = postService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postDTO);
    }

    /**
     * {@code DELETE  /posts/:id} : delete the "id" post.
     *
     * @param id the id of the postDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.debug("REST request to delete Post : {}", id);
        postService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping(value = "/posts/uploadPicture")
//    @PreAuthorize("hasRole(ROLE_ADMIN)")
    public ResponseEntity<PostDTO> uploadPicture(@RequestParam(name = "picture") MultipartFile file,
                                                 @RequestParam(name = "post_id") Long postId) {
        PostDTO postDTO = postService.uploadPicture(file, postId);
        if (!Objects.isNull(postDTO)) {
            return ResponseEntity.ok(postDTO);
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping(value = "/posts/viewPicture", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public ResponseEntity<byte[]> viewUniversityPicture(@RequestParam(name = "url") String url) throws IOException {
        return postService.getPicture(url);
    }

    @GetMapping("/posts/{id}/details")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        log.debug("REST request to get Post : {}", id);
        Optional<PostDTO> postDTO = postService.findOneWithEagerRelations(id);
        return ResponseUtil.wrapOrNotFound(postDTO);
    }
}
