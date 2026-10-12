package com.ficklebytes.service;


import com.ficklebytes.dto.CommentDto;
import com.ficklebytes.entity.Comment;
import com.ficklebytes.exception.CommentNotFoundException;
import com.ficklebytes.repository.CommentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {


  @Autowired
  private CommentRepository commentRepository;

  public static List<CommentDto> ListToDto(List<Comment> comments) {
    List<CommentDto> list = new ArrayList<>();
    for (Comment comment : comments) {
      list.add(ToDto(comment));
    }
    return list;
  }

  public static CommentDto ToDto(Comment comment) {
    return comment != null ? new CommentDto(comment) : null;
  }

  public Comment Save(Comment comment) {
    return commentRepository.save(comment);
  }

  public List<Comment> ListComments(int contentId) {
    List<Comment> rootComments=commentRepository.findByContentIdAndDeletedAndParentCommentIdIsNull(contentId,false);
    for (Comment comment:rootComments) {
          comment.setChildComments(commentRepository.findByParentCommentIdAndDeleted(comment.getId(),false));
    }
    return rootComments;
  }
public List<Comment> ListReplies(int parentCommentId)
{
  return commentRepository.findByParentCommentIdAndDeleted(parentCommentId,false);
}
  public Comment GetCommentById(int commentId) throws CommentNotFoundException {
    Optional<Comment> comment = commentRepository.findById(commentId);
    if (!comment.isPresent()) {
      throw new CommentNotFoundException("Invalid comment Id");
    }
    return comment.get();
  }


}
