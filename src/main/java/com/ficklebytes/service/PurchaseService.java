package com.ficklebytes.service;


import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Card;
import com.ficklebytes.entity.Course;
import com.ficklebytes.entity.Purchase;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.exception.CardAlreadyUsedException;
import com.ficklebytes.exception.CardNotFoundException;
import com.ficklebytes.exception.PurchasedCourseAlreadyException;
import com.ficklebytes.exception.UnAuthenticationException;
import com.ficklebytes.repository.CardRepository;
import com.ficklebytes.repository.PurchaseRepository;
import com.ficklebytes.utils.AuthUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PurchaseService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public boolean isUsed(int cardId) {
        return purchaseRepository.getPurchaseByCardId(cardId).isPresent();
    }

    public boolean activate(AcademyUser academyUser, Card card, Course course)
            throws UnAuthenticationException, CardNotFoundException,
            CardAlreadyUsedException, PurchasedCourseAlreadyException {
        AuthUtil.requiresLoggedIn(academyUser);
        if (!cardRepository.findById(card.getCardId()).isPresent()) {
            throw new CardNotFoundException("Card not found");
        }
        Optional<Purchase> purchaseUsingCard = purchaseRepository.getPurchaseByCardId(card.getCardId());
        if (purchaseUsingCard.isPresent()) {
            throw new CardAlreadyUsedException("Card is already used");
        }
        if (isPurchased(academyUser.getUserId(), course.getCourseId())) {
            throw new PurchasedCourseAlreadyException();
        }
        if (card.getPrice() != course.getPrice()) {
            return false;
        }
        Purchase purchase = new Purchase();
        purchase.setUserId(academyUser.getUserId());
        purchase.setCardId(card.getCardId());
        purchase.setDeleted(false);
        purchase.setCourseId(course.getCourseId());
        purchase.setActivationTimeDate(LocalDateTime.now(ZoneOffset.UTC));
        purchaseRepository.save(purchase);
        return true;
    }

    public boolean isPurchased(int userId, int courseId) {
        return purchaseRepository.existsByUserIdAndCourseIdAndDeletedFalse(userId,
                courseId);

    }

    public List<Purchase> findAllByCardIds(List<Integer> cardIds){
        return purchaseRepository.findAllByCardIds(cardIds);
    }

    public Optional<Purchase> getPurchaseByCardId(int cardId) {
        return purchaseRepository.getPurchaseByCardId(cardId);
    }

    public List<Purchase> getUserPurchases(int userId) {
        return purchaseRepository.getAllByUserId(userId).stream()
                .filter(purchase -> purchase.getCourse().getVisibility() == Visibility.PUBLIC)
                .collect(Collectors.toList());
    }

    public List<Purchase> findAllByCourseId(int courseId) {
        return purchaseRepository.findAllByCourseId(courseId);
    }

    public long countAllByCourseId(int courseId) {
        return purchaseRepository.countAllByCourseId(courseId);
    }
}
