package com.ficklebytes.controller;

import com.DtoMapper;
import com.ficklebytes.dto.ContentDto;
import com.ficklebytes.dto.LightContentDto;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Content;
import com.ficklebytes.entity.Course;
import com.ficklebytes.enums.ActivityName;
import com.ficklebytes.enums.ContentType;
import com.ficklebytes.enums.Role;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.exception.ContentNotFoundException;
import com.ficklebytes.exception.CourseNotFoundException;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.request.content.CloneContentsToCourses;
import com.ficklebytes.request.content.CreateLessonRequest;
import com.ficklebytes.request.content.CreateUnitRequest;
import com.ficklebytes.request.content.EditLessonRequest;
import com.ficklebytes.request.content.EditUnitRequest;
import com.ficklebytes.service.ActivityService;
import com.ficklebytes.service.ContentService;
import com.ficklebytes.service.CourseService;
import com.ficklebytes.service.PurchaseService;
import com.ficklebytes.utils.AuthUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/content")
@CrossOrigin
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ActivityService activityService;

    @GetMapping(value = "/list")
    public ResponseEntity<?> list(
            @AuthenticationPrincipal AcademyUser academyUser,
            @RequestParam("courseId") int courseId) throws CourseNotFoundException {

        // Step 1: Fetch the course (throws exception if not found)
        Course course = courseService.getCourseById(courseId);

        // Step 2: Determine allowed visibilities for this user
        boolean isAdmin = academyUser != null && academyUser.getRole() == Role.ADMIN;
        boolean isAuthor = academyUser != null && academyUser.getUserId() == course.getAuthorId();

        Set<Visibility> allowedVisibilities = (isAdmin || isAuthor)
                ? EnumSet.of(Visibility.PUBLIC, Visibility.PRIVATE)
                : EnumSet.of(Visibility.PUBLIC);

        // Step 3: Fetch content once (filtered directly in DB)
        List<Content> contents = contentService.GetAllContentsForCourseId(courseId, allowedVisibilities);

        // Step 4: Determine if user has premium access
        boolean premiumUser = (academyUser != null && (
                isAdmin || isAuthor || purchaseService.isPurchased(academyUser.getUserId(), courseId)
        ));

        // Step 5: Convert entities → DTOs (only set videoId if allowed)
        List<ContentDto> contentList = DtoMapper.toContentDtoList(contents, premiumUser);

        // Step 6: Return clean response
        return ResponseEntity.ok(contentList);
    }


    @GetMapping(value = "/list-with-unseen-comments-count/{courseId}")
    public ResponseEntity<?> listWithUnseenCommentsCount(
            @AuthenticationPrincipal AcademyUser academyUser,
            @PathVariable("courseId") int courseId)
            throws CourseNotFoundException, UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);
        Course course = courseService.getCourseById(courseId);
        Set<Visibility> visibilities =
                Stream.of(Visibility.PUBLIC).collect(Collectors.toSet());

        List<LightContentDto> contentList =
                DtoMapper.LightContentListDto(
                        contentService.GetAllContentsForCourseIdWithUnseenComments(courseId, visibilities));
        return ResponseEntity.ok(contentList);
    }


    @GetMapping(value = "/next")
    public ResponseEntity<?> next(@RequestParam("contentId") int contentId)
            throws ContentNotFoundException {
        Optional<Content> content =
                contentService.GetNextPublicVideo(contentId);
        return ResponseEntity.ok(content.map(Content::getContentId).orElse(-1));
    }

    @GetMapping(value = "/previous")
    public ResponseEntity<?> previous(@RequestParam("contentId") int contentId)
            throws ContentNotFoundException {
        Optional<Content> content =
                contentService.GetPreviousPublicVideo(contentId);
        return ResponseEntity.ok(content.map(Content::getContentId).orElse(-1));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@AuthenticationPrincipal AcademyUser academyUser,
                              @RequestParam("contentId") int contentId)
            throws ContentNotFoundException, UnAuthorizedException, CourseNotFoundException {
        ContentDto contentDto =
                DtoMapper.ToDto(contentService.GetContentById(contentId));
        Course course = courseService.getCourseById(contentDto.getCourseId());
        boolean premiumUser = (academyUser != null
                && (purchaseService.isPurchased(academyUser.getUserId(),
                contentDto.getCourseId())
                || academyUser.getRole() == Role.ADMIN
                || academyUser.getUserId() == course.getAuthorId()));
        if (!contentDto.isFree() && !premiumUser) {
            throw new UnAuthorizedException();
        }
        activityService.registerActivity(Objects.isNull(academyUser) ? -1 : academyUser.getUserId(),
                ActivityName.WATCH_LESSON, contentId);
        return ResponseEntity.ok(contentDto);

    }

    @PostMapping(value = "/create-unit")
    @CacheEvict(value = "contentCache", allEntries = true)
    public ResponseEntity<?> createUnit(@AuthenticationPrincipal AcademyUser academyUser,
                                     @RequestBody CreateUnitRequest request) throws UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);
        Content content = Content.builder()
                .courseId(request.getCourseId())
                .title(request.getTitle())
                .free(true)
                .description("")
                .creationTimeDate(LocalDateTime.now(ZoneOffset.UTC))
                .length(0)
                .order(contentService.GetNumberOfContents(request.getCourseId()))
                .type(ContentType.UNIT)
                .videoId("")
                .videoUrl("")
                .visibility(Visibility.PUBLIC)
                .build();
        content = contentService.Save(content);
        return ResponseEntity.ok(DtoMapper.ToDto(content));
    }

    @PostMapping(value = "/edit-unit")
    @CacheEvict(value = "contentCache", allEntries = true)
    public ResponseEntity<?> editUnit(@AuthenticationPrincipal AcademyUser academyUser,
                                   @RequestBody EditUnitRequest request)
            throws UnAuthorizedException, ContentNotFoundException {
        AuthUtil.requiresAdminRole(academyUser);
        if (!contentService.Exists(request.getContentId())) {
            throw new ContentNotFoundException();
        }
        Content content = contentService.GetContentById(request.getContentId());
        content.setTitle(request.getTitle());
        return ResponseEntity.ok(contentService.ToDto(contentService.Save(content)));
    }

    @PostMapping(value = "/create-lesson")
    @CacheEvict(value = "contentCache", allEntries = true)
    public ResponseEntity<?> createLesson(@AuthenticationPrincipal AcademyUser academyUser,
                                       @RequestBody CreateLessonRequest request) throws UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);
        Content content = Content.builder()
                .courseId(request.getCourseId())
                .title(request.getTitle())
                .free(request.isFree())
                .description("")
                .creationTimeDate(LocalDateTime.now(ZoneOffset.UTC))
                .length(0)
                .order(contentService.GetNumberOfContents(request.getCourseId()))
                .type(ContentType.LESSON)
                .videoId(request.getVideoId() != null ? request.getVideoId() : "")
                .videoUrl(request.getVideoId() != null ? request.getVideoId() : "")
                .visibility(request.getVisibility())
                .build();
        content = contentService.Save(content);
        return ResponseEntity.ok(DtoMapper.ToDto(content));
    }

    @PostMapping(value = "/edit-lesson")
    @CacheEvict(value = "contentCache", allEntries = true)
    public ResponseEntity<?> editLesson(@AuthenticationPrincipal AcademyUser academyUser,
                                     @RequestBody EditLessonRequest request)
            throws UnAuthorizedException, ContentNotFoundException {
        AuthUtil.requiresAdminRole(academyUser);
        if (!contentService.Exists(request.getContentId())) {
            throw new ContentNotFoundException();
        }
        Content content = contentService.GetContentById(request.getContentId());
        content.setVisibility(request.getVisibility());
        if (request.getVideoId() != null) {
            content.setVideoId(request.getVideoId());
        }
        content.setTitle(request.getTitle());
        content.setLength(0);
        content.setFree(request.isFree());
        content.setDescription("");

        return ResponseEntity.ok(contentService.ToDto(contentService.Save(content)));
    }

    @DeleteMapping(value = "/delete")
    @CacheEvict(value = "contentCache", allEntries = true)
    public ResponseEntity<?> delete(@AuthenticationPrincipal AcademyUser academyUser,
                                 @RequestParam("contentId") int contentId)
            throws ContentNotFoundException, UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);
        Content content = contentService.GetContentById(contentId);
        content.setVisibility(Visibility.DELETED);
        contentService.Save(content);
        return ResponseEntity.ok().build();
    }

//  @PostMapping(value = "/add")
//  public ResponseEntity add(@AuthenticationPrincipal AcademyUser academyUser,
//      @RequestBody() Content content)
// {

    /// /    AuthUtil.requiresAdminRole(academyUser);
//	  System.out.println(contentService.GetNumberOfContents(2));
//    content.setVisibility(Visibility.PUBLIC);
//    content.setCreationTimeDate(LocalDateTime.now());
//    contentService.Save(content);
//    return ResponseEntity.ok().body("Content Add Successfully");
//  }
//  
    @PostMapping(value = "/change-order")
    @CacheEvict(value = "contentCache", allEntries = true)
    public ResponseEntity<?> changeOrder(@AuthenticationPrincipal AcademyUser academyUser,
                                      @RequestBody List<Integer> contentIdList)
            throws ContentNotFoundException, UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);
        contentService.SetOrder(contentIdList);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/clone-contents-to-courses")
    public ResponseEntity<?> cloneContentsToCourses(@AuthenticationPrincipal AcademyUser academyUser,
                                                 @RequestBody CloneContentsToCourses request)
            throws ContentNotFoundException, UnAuthorizedException {
        //TODO MISSING CLONE DOCUMENT
        AuthUtil.requiresAdminRole(academyUser);
        for (Integer courseId : request.getCourseIdList()) {
            int count = contentService.GetNumberOfContents(courseId);
            for (Integer contentId : request.getContentIdList()) {
                Content content = contentService.GetContentById(contentId);
                Content clonedContent = Content.builder().courseId(courseId)
                        .creationTimeDate(LocalDateTime.now(ZoneOffset.UTC))
                        .description(content.getDescription())
                        .free(content.isFree())
                        .length(content.getLength())
                        .order(count++)
                        .title(content.getTitle())
                        .type(content.getType())
                        .videoId(content.getVideoId())
                        .videoUrl(content.getVideoUrl())
                        .visibility(content.getVisibility())
                        .build();
                contentService.Save(clonedContent);
            }
        }
        return ResponseEntity.ok().build();
    }

}
