package com.ficklebytes.repository;


import com.ficklebytes.entity.Card;
import com.ficklebytes.enums.Role;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository("CardRepository")
public interface CardRepository extends CrudRepository<Card, Integer> {

  Optional<Card> getCardByKeyIgnoreCase(String key);

  @Transactional
  @Modifying
  @Query("UPDATE Card c SET c.deleted = 1 WHERE c.cardId = :cardId")
  void deleteCard(@Param("cardId") Integer cardId);

  @Query("SELECT c.id FROM Card c WHERE c.deleted = 0")
  List<Integer> findAllCardIds();

  @Transactional
  @Modifying
  @Query("UPDATE Card c SET c.deleted = 1 WHERE c.cardId IN :cardIds")
  void deleteCardsByIds(@Param("cardIds") List<Integer> cardIds);

  @Query("SELECT c.id FROM Card c WHERE c.deleted = 0")
  List<Long> findDeletedCardIds();

}
