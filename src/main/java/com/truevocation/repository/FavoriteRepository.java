package com.truevocation.repository;

import com.truevocation.domain.Favorite;
import com.truevocation.domain.Likes;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Favorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query(value = "select count(favorite)>0 from Favorite favorite where " +
                   "favorite.post.id = :postId and favorite.user.id = :userId")
    boolean isFavorite(@Param("postId")Long postId, @Param("userId")Long userId);

    Favorite findByUserIdAndPostId(Long userId, Long postId);
}
