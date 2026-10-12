package com.ficklebytes.repository;


import com.ficklebytes.entity.Purchase;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository("PurchaseRepository")
public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {

    @Query("SELECT p FROM Purchase p WHERE p.cardId = :cardId AND p.deleted = 0")
    Optional<Purchase> getPurchaseByCardId(@Param("cardId") int cardId);

    @Query("SELECT p FROM Purchase p " +
            " LEFT JOIN FETCH p.user u " +
            " LEFT JOIN FETCH p.course c " +
            "  WHERE p.cardId IN :cardIds " +
      " AND p.deleted = 0 " )
    List<Purchase> findAllByCardIds(@Param("cardIds") List<Integer> cardIds);

    @Query("SELECT p FROM Purchase p WHERE p.userId = :userId AND p.deleted = 0")
    List<Purchase> getAllByUserId(@Param("userId") int userId);

    @Query("SELECT p FROM Purchase p WHERE p.courseId = :courseId AND p.deleted = 0")
    List<Purchase> findAllByCourseId(@Param("courseId") int courseId);

    @Query("SELECT COUNT(*) FROM Purchase p WHERE p.courseId = :courseId AND p.deleted = 0")
    long countAllByCourseId(@Param("courseId") int courseId);

//    @Query("SELECT p FROM Purchase p WHERE p.userId = :userId AND p.courseId = :courseId AND p.deleted = 0")
//    Optional<Purchase> getPurchaseByUserIdAndCourseId(@Param("userId") int userId, @Param("courseId") int courseId);

    @Query("SELECT COUNT(p) > 0 FROM Purchase p WHERE p.userId = :userId AND p.courseId = :courseId AND p.deleted = 0")
    boolean existsByUserIdAndCourseIdAndDeletedFalse(@Param("userId") int userId, @Param("courseId") int courseId);

    @Modifying
    @Transactional
    @Query("UPDATE Purchase p SET p.deleted = 1 WHERE p.cardId IN :cardIds")
    void deletePurchasesByCardIds(@Param("cardIds") List<Integer> cardIds);

    @Transactional
    @Modifying
    @Query("UPDATE Purchase p SET p.deleted = 1 WHERE p.cardId =:cardId")
    void deletePurchaseByCardId(@Param("cardId") int cardId);

}

