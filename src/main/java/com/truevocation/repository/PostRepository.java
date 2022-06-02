package com.truevocation.repository;

import com.truevocation.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Post entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByPictureEquals(String url);

    Page<Post> findAllByTitleContaining(String searchText, Pageable pageable);

    @Query(value = "select * from post order by created_date desc Limit 5", nativeQuery = true)
    List<Post> findAllOldPosts();
}
