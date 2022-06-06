package com.truevocation.repository;

import com.truevocation.domain.Comments;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Comments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    @Query(value = "select count(comment) from Comments comment where comment.post.id = :postId")
    int getPostCommentsCount(@Param("postId")Long postId);

    @Query(value = "select comment from Comments comment where comment.post.id = :postId")
    List<Comments> getPostComments(@Param("postId") Long postId);
}
