package com.truevocation.repository;

import com.truevocation.domain.Likes;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Likes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query(value = "select count(likes) from Likes likes where likes.post.id = :postId")
    int getPostLikesCount(@Param("postId") Long postId);

    @Query(value = "select count(likes) > 0 from Likes likes " +
                    "where likes.post.id = :postId and " +
                    "likes.user.id = :userId")
    boolean isLiked(@Param("postId")Long postId, @Param("userId")Long userId);

    Likes findByUserIdAndPostId(Long userId, Long postId);
}
