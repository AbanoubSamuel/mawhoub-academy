package com.ficklebytes.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ficklebytes.entity.Content;
import com.ficklebytes.entity.Course;
import com.ficklebytes.enums.ContentType;
import com.ficklebytes.enums.Role;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.request.content.CreateLessonRequest;
import com.ficklebytes.request.content.EditLessonRequest;
import com.ficklebytes.security.WithMockCustomUser;
import com.ficklebytes.service.ContentService;
import com.ficklebytes.service.CourseService;
import com.ficklebytes.service.PurchaseService;
import com.ficklebytes.service.UserService;
import com.ficklebytes.utils.Utils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
public class ContentControllerTest {

  @MockBean
  ContentService contentService;

  @MockBean
  UserService userService;

  @MockBean
  CourseService courseService;

  @MockBean
  PurchaseService purchaseService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockCustomUser(role = Role.STUDENT)
  public void add_lesson_content_as_user_then_it_should_return_unauthorized()
      throws
      Exception {
    mockMvc.perform(post("/api/content/create-lesson").
        content(Utils.ClassToString(CreateLessonRequest.builder()
            .build()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
  }

  @Test
  @WithMockCustomUser(role = Role.TEACHER)
  public void add_lesson_content_as_teacher_then_it_should_return_unauthorized()
      throws
      Exception {
    mockMvc.perform(post("/api/content/create-lesson").
        content(Utils.ClassToString(CreateLessonRequest.builder()
            .build()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
  }

  //TODO RE ENABLE THIS TEST
/*
    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void add_content_as_admin_then_it_should_return_ok() throws
    Exception {
        Content content = generateRandomContent();
        content.setContentId(0);
        CreateContentRequest createContentRequest = CreateContentRequest
        .builder()
                .courseId(content.getCourseId())
                .description(content.getDescription())
                .free(content.isFree())
                .length(content.getLength())
                .order(content.getOrder())
                .title(content.getTitle())
                .type(content.getType())
                .videoId(content.getVideoId())
                .visibility(content.getVisibility())
                .build();
        when(contentService.Save(any(Content.class))).thenReturn(content);
        MvcResult result = mockMvc.perform(post("/api/content/create").
                content(createContentRequest.toString()).
                contentType(MediaType.APPLICATION_JSON)).andExpect(status()
                .isOk()).andReturn();
        LocalDateTime creationTimeDate = JsonPath.read(result.getResponse()
        .getContentAsString(), "$.creationTimeDate");
        content.setCreationTimeDate(creationTimeDate);
        verify(contentService, Mockito.times(1)).Save(content);
    }
*/

  @Test
  @WithMockCustomUser(role = Role.STUDENT)
  public void edit_content_lesson_as_user_then_it_should_return_unauthorized()
      throws
      Exception {
    mockMvc.perform(post("/api/content/edit-lesson").
        content(Utils.ClassToString(CreateLessonRequest.builder()
            .build()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
  }

  @Test
  @WithMockCustomUser(role = Role.TEACHER)
  public void edit_content_lesson_as_teacher_then_it_should_return_unauthorized()
      throws
      Exception {
    mockMvc.perform(post("/api/content/edit-lesson").
        content(Utils.ClassToString(CreateLessonRequest.builder()
            .build()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
  }

  @Test
  @WithMockCustomUser(role = Role.ADMIN)
  public void edit_content_lesson_as_admin_then_it_should_return_ok()
      throws
      Exception {
    Content content = generateRandomContent();
    EditLessonRequest editUnitRequest = EditLessonRequest.builder()
        .contentId(content.getContentId())
        .free(content.isFree())
        .title(content.getTitle())
        .videoId(content.getVideoId())
        .visibility(content.getVisibility())
        .build();
    Content diffContent = generateRandomContent();
    diffContent.setContentId(content.getContentId());
    diffContent.setCourseId(content.getCourseId());
    diffContent.setCreationTimeDate(content.getCreationTimeDate());
    diffContent.setDocuments(content.getDocuments());
    diffContent.setOrder(content.getOrder());
    when(contentService.GetContentById(content.getContentId())).thenReturn(diffContent);
    when(contentService.Exists(content.getContentId())).thenReturn(true);
    mockMvc.perform(post("/api/content/edit-lesson").
        content(Utils.ClassToString(editUnitRequest))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(contentService,
        Mockito.times(1)).Save(content);
  }

  private Content generateRandomContent() {
    return Content.builder()
        .contentId(Utils.GenerateRandomNumber(0,
            100))
        .courseId(Utils.GenerateRandomNumber(0,
            100))
        .description("")
        .free(Utils.GenerateRandomBoolean())
        .length(0)
        .order(Utils.GenerateRandomNumber(0,
            100))
        .title(Utils.GenerateLowercaseString(5))
        .type(Utils.GenerateRandomBoolean() ? ContentType.UNIT :
            ContentType.LESSON)
        .videoId(Utils.GenerateLowercaseString(5))
        .visibility(Utils.GenerateRandomBoolean() ?
            Visibility.PUBLIC : Visibility.PRIVATE)
        .build();
  }

  @Test
  @WithMockCustomUser(role = Role.STUDENT)
  public void change_order_content_as_user_then_it_should_return_unauthorized()
      throws
      Exception {
    List<Integer> contentIdList = Arrays.asList(1,
        2);
    mockMvc.perform(post("/api/content/change-order")
        .content(contentIdList.toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    verify(contentService,
        times(0)).SetOrder(contentIdList);
  }

  @Test
  @WithMockCustomUser(role = Role.TEACHER)
  public void change_order_content_as_teacher_then_it_should_return_unauthorized()
      throws
      Exception {
    List<Integer> contentIdList = Arrays.asList(1,
        2);
    mockMvc.perform(post("/api/content/change-order")
        .content(contentIdList.toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    verify(contentService,
        times(0)).SetOrder(contentIdList);
  }

  @Test
  @WithMockCustomUser(role = Role.ADMIN)
  public void change_order_content_as_admin_then_it_should_return_ok()
      throws
      Exception {
    List<Integer> contentIdList = Arrays.asList(1,
        2);
    mockMvc.perform(post("/api/content/change-order")
        .content(contentIdList.toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(contentService,
        times(1)).SetOrder(contentIdList);
  }

  @Test
  @WithMockCustomUser(role = Role.STUDENT)
  public void delete_content_as_user_then_it_should_return_unauthorized()
      throws
      Exception {
    mockMvc.perform(delete("/api/content/delete")
        .param("contentId", "1"))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
  }

  @Test
  @WithMockCustomUser(role = Role.TEACHER)
  public void delete_content_as_teacher_then_it_should_return_unauthorized()
      throws
      Exception {
    mockMvc.perform(delete("/api/content/delete")
        .param("contentId", "1"))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
  }

  @Test
  @WithMockCustomUser(role = Role.ADMIN)
  public void delete_content_as_admin_then_it_should_return_ok()
      throws
      Exception {
    Content content = new Content();
    content.setContentId(1);
    when(contentService.GetContentById(content.getContentId())).thenReturn(content);
    when(contentService.Save(content)).thenReturn(content);
    mockMvc.perform(delete("/api/content/delete").param("contentId",
        content.getContentId() + ""))
        .andExpect(status().is(HttpStatus.OK.value()));
    content.setVisibility(Visibility.DELETED);
    verify(contentService, times(1)).Save(content);
  }

  @Test
  @WithMockCustomUser(role = Role.STUDENT, guest = true)
  public void get_free_content_as_guest_then_it_should_ok()
      throws
      Exception {
    verifyHttpStatusAndContentIfOk(1,
        false,
        true,
        HttpStatus.OK);
  }

  private void verifyHttpStatusAndContentIfOk(int authorId, boolean isPurchased,
      boolean isFree, HttpStatus httpStatus) throws Exception {
    Content content = generateRandomContent();
    content.setFree(isFree);
    content.setDocuments(new HashSet<>());
    Course course = Course.builder().authorId(authorId).build();
    when(courseService.getCourseById(content.getCourseId())).thenReturn(course);
    when(contentService.GetContentById(content.getContentId())).thenReturn(content);
    when(purchaseService.isPurchased(anyInt(),
        anyInt())).thenReturn(isPurchased);

    if (httpStatus == HttpStatus.OK) {
      MvcResult mvcResult = mockMvc.perform(get("/api/content/get").param("contentId",
          content.getContentId() + "").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().is(httpStatus.value())).andReturn();
      Assert.assertEquals(Utils.ClassToString(content),mvcResult.getResponse().getContentAsString());
    } else {
      mockMvc.perform(get("/api/content/get").param("contentId",
          content.getContentId() + ""))
          .andExpect(status().is(httpStatus.value()));
    }

  }

  @Test
  @WithMockCustomUser(role = Role.STUDENT,
      guest = true)
  public void get_paid_content_as_guest_then_it_should_unauthorized()
      throws
      Exception {
    verifyHttpStatusAndContentIfOk(1, false, false,
        HttpStatus.UNAUTHORIZED);
  }

  @Test
  @WithMockCustomUser(role = Role.STUDENT)
  public void get_premium_content_as_free_student_then_it_should_unauthorized()
      throws
      Exception {
    verifyHttpStatusAndContentIfOk(1337,
        false, false, HttpStatus.UNAUTHORIZED);
  }

  @Test
  @WithMockCustomUser(role = Role.STUDENT)
  public void get_free_content_as_free_student_then_it_should_ok()
      throws
      Exception {
    verifyHttpStatusAndContentIfOk(1337,
        false, true, HttpStatus.OK);
  }

  @Test
  @WithMockCustomUser(role = Role.STUDENT)
  public void get_premium_content_as_paid_student_then_it_should_ok()
      throws
      Exception {
    verifyHttpStatusAndContentIfOk(1337, true, false, HttpStatus.OK);
  }

  @Test
  @WithMockCustomUser(role = Role.TEACHER)
  public void get_premium_content_as_owner_teacher_then_it_should_ok()
      throws
      Exception {
    verifyHttpStatusAndContentIfOk(1, false, false, HttpStatus.OK);
  }

  @Test
  @WithMockCustomUser(role = Role.TEACHER)
  public void get_premium_content_as_normal_teacher_then_it_should_unauthorized()
      throws
      Exception {
    verifyHttpStatusAndContentIfOk(2, false, false, HttpStatus.UNAUTHORIZED);
  }

  @Test
  @WithMockCustomUser(role = Role.ADMIN)
  public void get_premium_content_as_admin_then_it_should_ok()
      throws
      Exception {
    verifyHttpStatusAndContentIfOk(2, false, false, HttpStatus.OK);
  }

  @Test
  @WithMockCustomUser(role = Role.STUDENT)
  public void list_contents_as_free_student_then_it_should_return_censored_public_lesson_lit()
      throws Exception {
    verifyCalledVisibilitySet(1337, true, Stream.of(Visibility.PUBLIC).collect(Collectors.toSet()));
  }

  private void verifyCalledVisibilitySet(int authorId,
      boolean isPurchased,
      Set<Visibility> visibilitySet)
      throws
      Exception {
    Course course = Course.builder()
        .courseId(1)
        .authorId(authorId)
        .build();

    when(courseService.getCourseById(course.getCourseId())).thenReturn(course);
    when(purchaseService.isPurchased(anyInt(),
        anyInt())).thenReturn(isPurchased);

    mockMvc.perform(get("/api/content/list").param("courseId",
        course.getCourseId() + ""))
        .andExpect(status().isOk());

    verify(contentService,
        times(1)).GetAllContentsForCourseId(course.getCourseId(),
        visibilitySet);
  }

  @Test
  @WithMockCustomUser(role = Role.TEACHER)
  public void list_contents_as_author_then_it_should_return_everything()
      throws
      Exception {
    verifyCalledVisibilitySet(1,
        false,
        Stream.of(Visibility.PRIVATE,
            Visibility.PUBLIC)
            .collect(Collectors.toSet()));
  }

  @Test
  @WithMockCustomUser(role = Role.ADMIN)
  public void list_contents_as_admin_then_it_should_return_everything()
      throws
      Exception {
    verifyCalledVisibilitySet(1337,
        false,
        Stream.of(Visibility.PRIVATE,
            Visibility.PUBLIC)
            .collect(Collectors.toSet()));
  }

}
