package com.truevocation.repository;

import com.truevocation.domain.Comments;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Comments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    @Query(value = "select count(comment) from Comments comment where post.id = :postId")
    int getPostCommentsCount(@Param("postId")Long postId);
}
