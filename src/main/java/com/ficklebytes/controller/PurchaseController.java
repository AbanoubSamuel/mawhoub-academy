package com.ficklebytes.controller;

import com.DtoMapper;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Card;
import com.ficklebytes.entity.Course;
import com.ficklebytes.exception.*;
import com.ficklebytes.request.purchase.ActivateCardRequest;
import com.ficklebytes.service.CardService;
import com.ficklebytes.service.CourseService;
import com.ficklebytes.service.PurchaseService;
import com.ficklebytes.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/purchase")
public class PurchaseController {

  @Autowired
  private PurchaseService purchaseService;

  @Autowired
  private CourseService courseService;

  @Autowired
  private CardService cardService;

  @GetMapping(value = "/is-purchased")
  public ResponseEntity purchased(@AuthenticationPrincipal AcademyUser academyUser,
      @RequestParam(name = "courseId") int courseId) throws UnAuthenticationException {
    AuthUtil.requiresLoggedIn(academyUser);
    return ResponseEntity.ok(purchaseService.isPurchased(academyUser.getUserId(), courseId));
  }

  @PostMapping(value = "/activate")
  @ResponseBody
  public ResponseEntity activate(@AuthenticationPrincipal AcademyUser academyUser,
      @RequestBody ActivateCardRequest request)
      throws CourseNotFoundException, UnAuthenticationException,
      PurchasedCourseAlreadyException {
    AuthUtil.requiresLoggedIn(academyUser);
    Course course = courseService.getCourseById(request.getCourseId());
    try {
      Card card = cardService.GetCardByKey(request.getCardKey());
      if (purchaseService.activate(academyUser, card, course)) {
        return ResponseEntity.ok().build();
      }
    } catch (CardNotFoundException | CardAlreadyUsedException ignored) {
    }
    return new ResponseEntity("Please Enter card information",
        HttpStatus.BAD_REQUEST);
  }


  @GetMapping(value = "/list-my-purchases")
  @ResponseBody
  public ResponseEntity listPurchases(@AuthenticationPrincipal AcademyUser academyUser)
      throws UnAuthenticationException {
    AuthUtil.requiresLoggedIn(academyUser);
    return ResponseEntity
        .ok(DtoMapper.PurchaseListDto(purchaseService.getUserPurchases(academyUser.getUserId())));
  }
  @GetMapping(value = "/course/{courseId}")
  @ResponseBody
  public ResponseEntity listPurchasesForCourse(@AuthenticationPrincipal AcademyUser academyUser, @PathVariable int courseId)
          throws UnAuthorizedException {
    AuthUtil.requiresAdminOrTeacherRoleOrLecturer(academyUser);
    return ResponseEntity
            .ok(DtoMapper.PurchaseWithoutCourseListDto(purchaseService.findAllByCourseId(courseId)));
  }

}
