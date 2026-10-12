package com.ficklebytes.service;


import com.ficklebytes.dto.ContentDto;
import com.ficklebytes.entity.Content;
import com.ficklebytes.enums.ContentType;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.exception.ContentNotFoundException;
import com.ficklebytes.repository.CommentRepository;
import com.ficklebytes.repository.ContentRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private ModelMapper modelMapper;

    public Optional<Content> GetNextPublicVideo(int contentId) throws ContentNotFoundException {
        List<Content> contents =
                contentRepository.findAllByVisibilityEqualsAndCourseIdEqualsOrderByOrder(Visibility.PUBLIC,
                                GetContentById(contentId).getCourseId())
                        .stream().filter(content -> content.getType() == ContentType.LESSON)
                        .collect(Collectors.toList());
        int index =
                contents.stream().map(Content::getContentId).collect(Collectors.toList())
                        .indexOf(contentId);
        if (index + 1 == contents.size() || index == -1) {
            return Optional.empty();
        }
        return Optional.of(contents.get(index + 1));
    }

    public Content GetContentById(int contentId) throws ContentNotFoundException {
        Content content = contentRepository.findById(contentId).orElse(null);
        if (content == null) {
            throw new ContentNotFoundException();
        }
        return content;
    }

    public Optional<Content> GetPreviousPublicVideo(int contentId) throws ContentNotFoundException {
        List<Content> contents =
                contentRepository.findAllByVisibilityEqualsAndCourseIdEqualsOrderByOrder(Visibility.PUBLIC,
                                GetContentById(contentId).getCourseId())
                        .stream().filter(content -> content.getType() == ContentType.LESSON)
                        .collect(Collectors.toList());
        int index =
                contents.stream().map(Content::getContentId).collect(Collectors.toList())
                        .indexOf(contentId);
        if (index == 0 || index == -1) {
            return Optional.empty();
        }
        return Optional.of(contents.get(index - 1));
    }

    @Cacheable(
            value = "contentCache",
            key = "T(java.lang.String).valueOf(#courseId) + '_' + T(java.util.Objects).hash(#visibilityList)"
    )
    public List<Content> GetAllContentsForCourseId(int courseId,
                                                   Set<Visibility> visibilityList) {
        return getAllContentsForCourseIdWithVisibility(courseId, visibilityList);
    }


    public List<Content> GetAllContentsForCourseIdWithUnseenComments(int courseId,
                                                                     Set<Visibility> visibilityList) {
        List<Content> contents = getAllContentsForCourseIdWithVisibility(courseId, visibilityList);
        for (Content content : contents) {
            content.setUnseenCommentsCount(commentRepository
                    .countByContentIdAndSeenByTeacherAndDeleted(content.getContentId(), false, false));
        }
        return contents;
    }

    private List<Content> getAllContentsForCourseIdWithVisibility(int courseId,
                                                                  Set<Visibility> visibilityList) {
        return contentRepository.findAllByCourseIdWithDocumentsAndVisibility(courseId, visibilityList);
//        .stream()
//        .filter(content -> visibilityList.contains(content.getVisibility()))
//        .collect(Collectors.toList());
//    contents.sort((t1, t2) -> {
//      if (t1.getOrder() == t2.getOrder()) {
//        return 0;
//      }
//      return t1.getOrder() < t2.getOrder() ? -1 : 1;
//    });
//    return contents;
    }

    public void SetOrder(List<Integer> contentIdList) throws ContentNotFoundException {
        for (int orderIndex = 0; orderIndex < contentIdList.size(); ++orderIndex) {
            Content content = GetContentById(contentIdList.get(orderIndex));
            content.setOrder(orderIndex);
            Save(content);
        }
    }

    public int GetNumberOfContents(int courseId) {
        return contentRepository.countByCourseId(courseId);
    }

    public Content Save(Content content) {
        return contentRepository.save(content);
    }

    public boolean Exists(int contentId) {
        return contentRepository.existsById(contentId);
    }

    public ContentDto ToDto(Content content) {
        return modelMapper.map(content, ContentDto.class);
    }

}
