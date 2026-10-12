package com.ficklebytes.controller;

import com.DtoMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.dto.CardDto;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Card;
import com.ficklebytes.entity.Purchase;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.request.card.CreateCardRequest;
import com.ficklebytes.response.APIResponse;
import com.ficklebytes.service.CardService;
import com.ficklebytes.service.PurchaseService;
import com.ficklebytes.utils.AuthUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@RequestMapping("/api/card" )
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping(value = "/create" )
    @ResponseBody
    public ResponseEntity create(@AuthenticationPrincipal AcademyUser academyUser,
                                 @RequestBody CreateCardRequest request) throws UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);
        List<Card> cardArrayList = new ArrayList<>();
        for (int i = 0; i < request.getAmount(); ++i) {
            cardArrayList.add(cardService.CreateCard(request));
        }
        return ResponseEntity.ok().body(cardArrayList);
    }

    @DeleteMapping(value = "/delete" )
    public ResponseEntity delete(@AuthenticationPrincipal AcademyUser academyUser,
                                 @RequestParam int id) throws UnAuthorizedException {
        AuthUtil.requiresOwnerOnly(academyUser);
        return ResponseEntity.ok().body(cardService.deleteCard(id));
    }

    @DeleteMapping(value = "/delete-all" )
    public ResponseEntity<APIResponse> deleteAll(@AuthenticationPrincipal AcademyUser academyUser) throws UnAuthorizedException {
        AuthUtil.requiresOwnerOnly(academyUser);
        return ResponseEntity.ok().body(cardService.deleteAllCards());
    }

    @GetMapping("/list" )
    public ResponseEntity<?> list(@AuthenticationPrincipal AcademyUser academyUser) throws UnAuthorizedException {
    AuthUtil.requiresAdminRole(academyUser);

        // 1️⃣ Load all cards
        List<Card> cards = cardService.listAllCards();
        if (cards.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // 2️⃣ Collect all card IDs
        List<Integer> cardIds = cards.stream()
                .map(Card::getCardId)
                .collect(Collectors.toList());

        // 3️⃣ Fetch all purchases for these cards in one query
        List<Purchase> purchases = purchaseService.findAllByCardIds(cardIds);

        // 4️⃣ Map cardId -> Purchase
        Map<Integer, Purchase> purchaseMap = purchases.stream()
                .collect(Collectors.toMap(Purchase::getCardId, Function.identity()));

        // 5️⃣ Map to DTOs
        List<CardDto> cardDtos = cards.stream()
                .map(card -> {
                    CardDto dto = new CardDto();
                    dto.setCardId(card.getCardId());
                    dto.setKey(card.getKey());
                    dto.setUsed(false);
                    dto.setCreationTimeDate(card.getCreationTimeDate());
                    dto.setPrice(card.getPrice());
                    Purchase p = purchaseMap.get(card.getCardId());
                    if (p != null) {
                        dto.setUsed(true);
                        dto.setBuyerUsername(p.getUser().getUsername());
                        dto.setCourseName(p.getCourse().getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(cardDtos);
    }

}
