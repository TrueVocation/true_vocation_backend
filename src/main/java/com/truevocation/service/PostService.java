package com.truevocation.service;

import com.truevocation.service.dto.PostDTO;
import com.truevocation.service.dto.PostsPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.truevocation.domain.Post}.
 */
public interface PostService {
    /**
     * Save a post.
     *
     * @param postDTO the entity to save.
     * @return the persisted entity.
     */
    PostDTO save(PostDTO postDTO);

    /**
     * Partially updates a post.
     *
     * @param postDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostDTO> partialUpdate(PostDTO postDTO);

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostDTO> findAll(Pageable pageable);

    Page<PostDTO> findAllToday(Pageable pageable, LocalDate date);

    /**
     * Get the "id" post.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostDTO> findOne(Long id);

    /**
     * Delete the "id" post.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    PostDTO uploadPicture(MultipartFile file, Long postId);

    ResponseEntity<byte[]> getPicture(String url) throws IOException;

    HttpHeaders addCustomPaginationHeaders(HttpHeaders headers, Page<PostDTO> page);

    PostsPageDTO findAllForPostsPage(String searchText, Pageable pageable);

    Optional<PostDTO> findOneWithEagerRelations(Long id);
}
