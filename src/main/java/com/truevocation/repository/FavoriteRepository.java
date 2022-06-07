package com.truevocation.repository;

import com.truevocation.domain.AppUser;
import com.truevocation.domain.Favorite;
import com.truevocation.domain.University;
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

    @Query(value = "select count(favorite)>0 from Favorite favorite where " +
                   "favorite.university.id = :universityId and favorite.user.id = :userId")
    boolean isFavoriteUniversity(@Param("universityId")Long universityId, @Param("userId")Long userId);

    @Query(value = "select count(favorite)>0 from Favorite favorite where " +
                   "favorite.specialty.id = :specialtyId and favorite.user.id = :userId")
    boolean isFavoriteSpeciality(@Param("specialtyId")Long specialtyId, @Param("userId")Long userId);

    @Query(value = "select count(favorite)>0 from Favorite favorite where " +
                   "favorite.profession.id = :professionId and favorite.user.id = :userId")
    boolean isFavoriteProfession(@Param("professionId")Long professionId, @Param("userId")Long userId);


    @Modifying
    @Query(value = "delete from Favorite favorite where " +
                   "favorite.profession.id = :professionId and favorite.user.id = :userId")
    void deleteFavoriteProfession(@Param("professionId")Long professionId, @Param("userId")Long userId);


    @Modifying
    @Query(value = "delete from Favorite favorite where " +
                   "favorite.specialty.id = :specialtyId and favorite.user.id = :userId")
    void deleteFavoriteSpeciality(@Param("specialtyId")Long specialtyId, @Param("userId")Long userId);

    @Modifying
    @Query(value = "delete from Favorite favorite where " +
                   "favorite.university.id = :universityId and favorite.user.id = :userId")
    void deleteFavoriteUniversity(@Param("universityId")Long universityId, @Param("userId")Long userId);

    Favorite findByUserIdAndPostId(Long userId, Long postId);
}
