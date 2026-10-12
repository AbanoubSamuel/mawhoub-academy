package com.ficklebytes.controller;

import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Comment;
import com.ficklebytes.entity.Content;
import com.ficklebytes.entity.Course;
import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.CommentNotFoundException;
import com.ficklebytes.exception.ContentNotFoundException;
import com.ficklebytes.exception.CourseNotFoundException;
import com.ficklebytes.exception.UnAuthenticationException;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.request.comment.AddCommentRequest;
import com.ficklebytes.service.CommentService;
import com.ficklebytes.service.ContentService;
import com.ficklebytes.service.CourseService;

import com.ficklebytes.utils.AuthUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CourseService courseService;

   

  

    @Autowired
    private ContentService contentService;

    @GetMapping(value = "/list")
    public ResponseEntity list(@AuthenticationPrincipal AcademyUser academyUser, @RequestParam("contentId") int contentId)
            throws ContentNotFoundException {
        if (!contentService.Exists(contentId)) {
            throw new ContentNotFoundException();
        }
        List<Comment> commentsOfTheContent = commentService.ListComments(contentId);

        return ResponseEntity.ok(CommentService.ListToDto(commentsOfTheContent));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@AuthenticationPrincipal AcademyUser academyUser,
                                 @RequestParam("commentId") int commentId)
            throws UnAuthenticationException, CommentNotFoundException,
            UnAuthorizedException {
        AuthUtil.requiresLoggedIn(academyUser);
        Comment comment = commentService.GetCommentById(commentId);
        if (comment.getAuthorId() != academyUser.getUserId() && academyUser.getRole() != Role.ADMIN) {
            throw new UnAuthorizedException();
        }
        comment.setDeleted(true);
        List<Comment> replies =commentService.ListReplies(commentId);
        for(Comment reply : replies)
        {
            reply.setDeleted(true);
            commentService.Save(reply);
        }
        commentService.Save(comment);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/mark-comment-as-seen-by-teacher/{courseId}/{commentId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity markCommentAsSeenByTeacher(@AuthenticationPrincipal AcademyUser academyUser,
                                                     @PathVariable int courseId,
                                                     @PathVariable int commentId) throws UnAuthorizedException,
                                                     CommentNotFoundException, CourseNotFoundException {
        AuthUtil.requiresAdminOrTeacherRole(academyUser);
        Course course = courseService.getCourseById(courseId);
        Comment comment = commentService.GetCommentById(commentId);
        if(course.getAuthorId() != academyUser.getUserId() && academyUser.getRole()!=Role.ADMIN)
        {
            throw new UnAuthorizedException();
        }
        comment.setSeenByTeacher(true);
        commentService.Save(comment);
        return ResponseEntity.ok().build();

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity add(@AuthenticationPrincipal AcademyUser academyUser,
                              @RequestBody AddCommentRequest request)
            throws CommentNotFoundException, ContentNotFoundException,
            UnAuthenticationException, CourseNotFoundException {
        AuthUtil.requiresLoggedIn(academyUser);
        if (request.getParentCommentId() != null) {
            Comment comment = commentService.GetCommentById(request.getParentCommentId().intValue());
        }
        Content content = contentService.GetContentById(request.getContentId());
//        Course course = courseService.getCourseById(content.getCourseId());
        Comment comment = Comment.builder()
                .authorId(academyUser.getUserId())
                .author(academyUser)
                .parentCommentId(request.getParentCommentId())
                .deleted(false)
                .anonymous(request.isAnonymous())
                .creationTimestamp(LocalDateTime.now(ZoneOffset.UTC))
                .content(content)
                .description(request.getDescription())
                .contentId(content.getContentId())
                .build();

        return ResponseEntity.ok(commentService.Save(comment));
    }




}
