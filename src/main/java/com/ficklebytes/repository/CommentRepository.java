package com.ficklebytes.repository;


import com.ficklebytes.entity.Comment;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Integer> {

  List<Comment> findAllByContentIdAndDeletedAndParentCommentId(int contentId, boolean deleted,
      int parentCommentId);
  List<Comment> findByContentIdAndDeletedAndParentCommentIdIsNull(int contentId,boolean deleted);
  List<Comment> findByParentCommentIdAndDeleted(int parentCommentId,boolean deleted);
  long countByContentIdAndSeenByTeacherAndDeleted(int contentId, boolean seenByTeacher, boolean deleted);
}

