package com.truevocation.repository;

import com.truevocation.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(
        value = "select u from University u " +
            "inner join Favorite f on u.id = f.university.id " +
            "where f.user.id = :id"
    )
    Page<University> findAllFavoriteUniversityByUserId(Pageable pageable, @Param("id") Long id);

    @Query(
        value = "select s from Specialty s " +
            "inner join Favorite f on s.id = f.specialty.id " +
            "where f.user.id = :id"
    )
    Page<Specialty> findAllFavoriteSpecialtyByUserId(Pageable pageable, @Param("id") Long id);

    @Query(
        value = "select p from Profession p " +
            "inner join Favorite f on p.id = f.profession.id " +
            "where f.user.id = :id"
    )
    Page<Profession> findAllFavoriteProfessionByUserId(Pageable pageable, @Param("id") Long id);

    @Query(
        value = "select p from Post p " +
            "inner join Favorite f on p.id = f.post.id " +
            "where f.user.id = :id"
    )
    Page<Post> findAllFavoritePostByUserId(Pageable pageable, @Param("id") Long id);
}
