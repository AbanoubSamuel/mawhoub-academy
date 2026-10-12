package com.ficklebytes.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Card;
import com.ficklebytes.entity.Course;
import com.ficklebytes.entity.Purchase;
import com.ficklebytes.exception.CardAlreadyUsedException;
import com.ficklebytes.exception.CardNotFoundException;
import com.ficklebytes.exception.PurchasedCourseAlreadyException;
import com.ficklebytes.exception.UnAuthenticationException;
import com.ficklebytes.repository.CardRepository;
import com.ficklebytes.repository.PurchaseRepository;
import com.ficklebytes.service.PurchaseService;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
public class PurchaseServiceTest {

  @MockBean
  private CardRepository cardRepository;

  @MockBean
  private PurchaseRepository purchaseRepository;

  @Autowired
  private PurchaseService purchaseService;

  @Test(expected = UnAuthenticationException.class)
  public void activate_and_not_signed_in_should_throw_unauthenticated_exception()
      throws Exception {
    purchaseService.activate(null, Card.builder().build(), Course.builder().build());
  }

  @Test(expected = CardNotFoundException.class)
  public void activate_and_signed_in_and_find_card_by_id_returns_null_should_throw_card_not_found_exception()
      throws Exception {
    Optional<Card> card = Optional.empty();
    when(cardRepository.findById(anyInt())).thenReturn(card);
    purchaseService
        .activate(AcademyUser.builder().build(), Card.builder().build(), Course.builder().build());
  }

  @Test(expected = CardAlreadyUsedException.class)
  public void activate_and_signed_in_and_card_already_used_should_throw_card_already_used_exception()
      throws Exception {
    Optional<Purchase> purchase = Optional.of(mock(Purchase.class));
    Optional<Card> card = Optional.of(mock(Card.class));
    when(cardRepository.findById(anyInt())).thenReturn(card);
    when(purchaseRepository.getPurchaseByCardId(anyInt())).thenReturn(purchase);
    purchaseService.activate(AcademyUser.builder().build(), card.get(), Course.builder().build());
  }

  @Test(expected = PurchasedCourseAlreadyException.class)
  public void activate_and_signed_in_and_course_already_purchased_should_throw_purchased_course_already_exception()
      throws Exception {
    Optional<Purchase> emptyPurchase = Optional.empty();
    Optional<Purchase> userPurchase = Optional.of(Purchase.builder().build());
    Optional<Card> card = Optional.of(Card.builder().build());
    when(cardRepository.findById(anyInt())).thenReturn(card);
    when(purchaseRepository.getPurchaseByCardId(anyInt())).thenReturn(emptyPurchase);
    when(purchaseRepository.existsByUserIdAndCourseIdAndDeletedFalse(anyInt(),
        anyInt())).thenReturn(true);
    purchaseService
        .activate(AcademyUser.builder().build(), Card.builder().build(), Course.builder().build());
  }

  @Test
  public void activate_and_signed_in_and_course_price_not_equals_card_price_should_return_false()
      throws Exception {
    Optional<Purchase> emptyPurchase = Optional.empty();
    Optional<Card> card = Optional.of(Card.builder().build());
    when(cardRepository.findById(anyInt())).thenReturn(card);
    when(purchaseRepository.getPurchaseByCardId(anyInt())).thenReturn(emptyPurchase);
    when(purchaseRepository.existsByUserIdAndCourseIdAndDeletedFalse(anyInt(),
        anyInt())).thenReturn(true);
    assertFalse(purchaseService
        .activate(AcademyUser.builder().build(), Card.builder().price(1).build(),
            Course.builder().price(2).build()));
  }

  @Test
  public void activate_and_signed_in_and_course_price_equals_card_price_should_return_true_and_save_should_be_called()
      throws Exception {
    Optional<Purchase> emptyPurchase = Optional.empty();
    Optional<Card> card = Optional.of(Card.builder().build());
    when(cardRepository.findById(anyInt())).thenReturn(card);
    when(purchaseRepository.getPurchaseByCardId(anyInt())).thenReturn(emptyPurchase);
    when(purchaseRepository.existsByUserIdAndCourseIdAndDeletedFalse(anyInt(),
        anyInt())).thenReturn(true);
    assertTrue(purchaseService.activate(AcademyUser.builder().build(),
        Card.builder().price(1).build(),
        Course.builder().price(1).build()));
    verify(purchaseRepository).save(any(Purchase.class));
  }

}
