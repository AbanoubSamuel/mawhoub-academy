package com.ficklebytes.controller;

import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Card;
import com.ficklebytes.entity.Course;
import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.CardAlreadyUsedException;
import com.ficklebytes.exception.CardNotFoundException;
import com.ficklebytes.exception.CourseNotFoundException;
import com.ficklebytes.exception.PurchasedCourseAlreadyException;
import com.ficklebytes.request.purchase.ActivateCardRequest;
import com.ficklebytes.security.WithMockCustomUser;
import com.ficklebytes.service.CardService;
import com.ficklebytes.service.CourseService;
import com.ficklebytes.service.PurchaseService;
import com.ficklebytes.util.TestUtil;
import com.ficklebytes.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)@SpringBootTest@ContextConfiguration@AutoConfigureMockMvc
public class PurchaseControllerTest {

    @MockBean
    private PurchaseService purchaseService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private CardService cardService;@Autowired
    private MockMvc mockMvc;

    @Test
    public void is_purchased_not_signed_in_then_it_should_return_unauthorized()
        throws
        Exception {
        mockMvc.perform(get("/api/purchase/is-purchased?courseId=1"))
            .andExpect(status().is(HttpStatus.FORBIDDEN.value()));

    }

    @Test@WithMockCustomUser(role = Role.STUDENT)
    public void is_purchased_signed_then_it_should_return_ok()
        throws
        Exception {
        mockMvc.perform(get("/api/purchase/is-purchased?courseId=1"))
            .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void activate_not_signed_then_it_should_return_forbidden()
        throws
        Exception {
        ActivateCardRequest activateCardRequest = TestUtil.generateActivateCardRequest();
        mockMvc.perform(post("/api/purchase/activate")
            .content(Utils.ClassToString(activateCardRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test@WithMockCustomUser(role = Role.STUDENT)
    public void activate_signed_in_and_course_does_not_exist_then_it_should_return_not_found()
        throws
        Exception {

        ActivateCardRequest activateCardRequest = TestUtil.generateActivateCardRequest();
        when(courseService.getCourseById(activateCardRequest.getCourseId())).thenThrow(CourseNotFoundException.class);
        mockMvc.perform(post("/api/purchase/activate")
            .content(Utils.ClassToString(activateCardRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test@WithMockCustomUser(role = Role.STUDENT)
    public void activate_signed_in_and_course_already_is_purchased_then_it_should_return_conflict()
        throws
        Exception {
        ActivateCardRequest activateCardRequest = TestUtil.generateActivateCardRequest();
        when(courseService.getCourseById(activateCardRequest.getCourseId())).thenReturn(mock(Course.class));
        when(cardService.GetCardByKey(activateCardRequest.getCardKey())).thenReturn(mock(Card.class));
        when(purchaseService.activate(any(AcademyUser.class), any(Card.class), any(Course.class)))
            .thenThrow(PurchasedCourseAlreadyException.class);
        mockMvc.perform(post("/api/purchase/activate")
            .content(Utils.ClassToString(activateCardRequest)
            ).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @Test@WithMockCustomUser(role = Role.STUDENT)
    public void activate_signed_in_and_course_is_not_purchased_then_it_should_return_ok()
        throws
        Exception {
        ActivateCardRequest activateCardRequest = TestUtil.generateActivateCardRequest();
        when(courseService.getCourseById(activateCardRequest.getCourseId())).thenReturn(mock(Course.class));
        when(cardService.GetCardByKey(activateCardRequest.getCardKey())).thenReturn(mock(Card.class));
        when(purchaseService.activate(any(AcademyUser.class), any(Card.class), any(Course.class))).thenReturn(true);
        mockMvc.perform(post("/api/purchase/activate")
            .content(Utils.ClassToString(activateCardRequest))
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test@WithMockCustomUser(role = Role.STUDENT)
    public void activate_signed_in_and_card_does_not_exist_then_it_should_return_bad_request()
        throws
        Exception {
        ActivateCardRequest activateCardRequest = TestUtil.generateActivateCardRequest();
        when(courseService.getCourseById(activateCardRequest.getCourseId())).thenReturn(mock(Course.class));
        when(cardService.GetCardByKey(activateCardRequest.getCardKey())).thenReturn(mock(Card.class));
        when(purchaseService.activate(any(AcademyUser.class), any(Card.class), any(Course.class))).thenThrow(CardNotFoundException.class);
        mockMvc.perform(post("/api/purchase/activate")
            .content(Utils.ClassToString(activateCardRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test@WithMockCustomUser(role = Role.STUDENT)
    public void activate_signed_in_and_card_is_used_then_it_should_return_bad_request()
        throws Exception {
        ActivateCardRequest activateCardRequest = TestUtil.generateActivateCardRequest();
        when(courseService.getCourseById(activateCardRequest.getCourseId())).thenReturn(mock(Course.class));
        when(cardService.GetCardByKey(activateCardRequest.getCardKey())).thenReturn(mock(Card.class));
        when(purchaseService.activate(any(AcademyUser.class), any(Card.class), any(Course.class)))
            .thenThrow(CardAlreadyUsedException.class);
        mockMvc.perform(post("/api/purchase/activate").
            content(Utils.ClassToString(activateCardRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void list_purchases_and_not_signed_in_then_it_should_return_forbidden()
        throws Exception {
        mockMvc.perform(get("/api/purchase/list-my-purchases")).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test@WithMockCustomUser(role = Role.STUDENT)
    public void list_purchases_and_signed_in_then_it_should_return_ok()
        throws Exception {
        mockMvc.perform(get("/api/purchase/list-my-purchases")).andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void list_purchases_for_course_and_not_signed_in_then_it_should_return_unauthorized()
        throws Exception {
        mockMvc.perform(get("/api/purchase/course/1")).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test@WithMockCustomUser(role = Role.STUDENT)
    public void list_purchases_for_course_and_signed_in_as_student_then_it_should_return_unauthorized()
        throws Exception {
        mockMvc.perform(get("/api/purchase/course/1")).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test@WithMockCustomUser(role = Role.TEACHER)
    public void list_purchases_for_course_and_signed_in_as_teacher_then_it_should_return_ok()
        throws Exception {
        mockMvc.perform(get("/api/purchase/course/1")).andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test@WithMockCustomUser(role = Role.ADMIN)
    public void list_purchases_for_course_and_signed_in_as_admin_then_it_should_return_ok()
        throws Exception {
        mockMvc.perform(get("/api/purchase/course/1")).andExpect(status().is(HttpStatus.OK.value()));
    }
}
