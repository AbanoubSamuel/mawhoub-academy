package com.ficklebytes.service;


import com.cloudinary.api.exceptions.NotFound;
import com.ficklebytes.entity.Card;
import com.ficklebytes.entity.Purchase;
import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.CardNotFoundException;
import com.ficklebytes.repository.CardRepository;
import com.ficklebytes.repository.CourseRepository;
import com.ficklebytes.repository.PurchaseRepository;
import com.ficklebytes.request.card.CreateCardRequest;
import com.ficklebytes.response.APIResponse;
import com.ficklebytes.utils.Utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Card> listAllCards() {
        return (List<Card>) cardRepository.findAll();
    }

    public Card GetCardByKey(String key) throws CardNotFoundException {
        Card card = cardRepository.getCardByKeyIgnoreCase(key).orElse(null);
        if (card == null) {
            throw new CardNotFoundException("Invalid Card Key" );
        }
        return card;
    }

    public Card CreateCard(CreateCardRequest cardRequest) {
        Card card =
                Card.builder().creationTimeDate(LocalDateTime.now(ZoneOffset.UTC))
                        .key(GenerateRandomKey())
                        .price(cardRequest.getPrice())
                        .build();
        return cardRepository.save(card);
    }

    public APIResponse deleteCard(int cardId) {
        APIResponse apiResponse = new APIResponse();
        try {
            boolean exist = cardRepository.existsById(cardId);
            if (exist) {
                purchaseRepository.deletePurchaseByCardId(cardId);
                cardRepository.deleteCard(cardId);
                apiResponse.setAll(200, "Card deleted successfully", null);
            } else {
                apiResponse.setAll(404, "Card not found", null);
            }
        } catch (Exception e) {
            apiResponse.setAll(404, e.getMessage(), null);
        }
        return apiResponse;
    }


    private String GenerateRandomKey() {
        String key = Utils.GenerateLowercaseString(16);
        while (IsExists(key)) {
            key = Utils.GenerateLowercaseString(16);
        }
        return key;
    }

    public boolean IsExists(String key) {
        return cardRepository.getCardByKeyIgnoreCase(key).isPresent();
    }

    @Transactional
    public APIResponse deleteAllCards() {
        APIResponse apiResponse = new APIResponse();
        try {
            List<Integer> cardIds = cardRepository.findAllCardIds();

            if (cardIds.isEmpty()) {
                apiResponse.setAll(404, "No cards found", Collections.EMPTY_LIST);
                return apiResponse;
            }

            purchaseRepository.deletePurchasesByCardIds(cardIds);

            cardRepository.deleteCardsByIds(cardIds);

            apiResponse.setAll(200, "All cards deleted successfully", Collections.EMPTY_LIST);
        } catch (Exception e) {
            apiResponse.setAll(500, e.getMessage(), null);
        }
        return apiResponse;
    }

    public List<Long> getDeletedCardIds() {
        return cardRepository.findDeletedCardIds();
    }
}
