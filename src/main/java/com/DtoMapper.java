package com;

import com.ficklebytes.dto.*;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Card;
import com.ficklebytes.entity.Content;
import com.ficklebytes.entity.Course;
import com.ficklebytes.entity.Document;
import com.ficklebytes.entity.Lecture;
import com.ficklebytes.entity.Purchase;
import com.ficklebytes.entity.School;
import com.ficklebytes.entity.Study;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ficklebytes.enums.Visibility;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

@UtilityClass
public class DtoMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
    }


    public static List<CardDto> CardListDto(List<Card> cards) {
        return cards.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

    public static CardDto ToDto(Card card) {
        return modelMapper.map(card, CardDto.class);
    }

    public static List<StudyDto> StudiesListDto(List<Study> studies) {
        return studies.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

    public static StudyDto ToDto(Study study) {
        return modelMapper.map(study, StudyDto.class);
    }


    public static List<SchoolDto> SchoolsListDto(List<School> schools) {
        return schools.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

    public static SchoolDto ToDto(School school) {
        return modelMapper.map(school, SchoolDto.class);
    }

    public static List<CourseDto> CoursesListDto(List<Course> courses) {
        return courses.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

    public static List<LectureDto> LecturesListDto(List<Lecture> lectures) {
        return lectures.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

//	  public static CourseDto ToDto(Course course) {
//	    return modelMapper.map(course, CourseDto.class);
//	  }


    public static List<PurchaseDto> PurchaseListDto(List<Purchase> purchases) {
        return purchases.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

    public static PurchaseDto ToDto(Purchase purchase) {
        return modelMapper.map(purchase, PurchaseDto.class);
    }

    public static List<PurchaseWithoutCourseDto> PurchaseWithoutCourseListDto(List<Purchase> purchases) {
        return purchases.stream().map(DtoMapper::ToPurchaseWithoutCourseDto).collect(Collectors.toList());
    }

    public static PurchaseWithoutCourseDto ToPurchaseWithoutCourseDto(Purchase purchase) {
        return modelMapper.map(purchase, PurchaseWithoutCourseDto.class);
    }


    public List<UserDto> UserListDto(List<AcademyUser> users) {
        return users.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

    public List<CustomUserDto> CustomUserListDto(List<AcademyUser> users) {
        return users.stream().map(DtoMapper::customUserDto).collect(Collectors.toList());
    }

    public CustomUserDto customUserDto(AcademyUser user) {
        return modelMapper.map(user, CustomUserDto.class);
    }

    public UserDto ToDto(AcademyUser user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static List<CourseDto> CourseListDto(List<Course> courses) {
        return courses.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

    public CourseWithoutAuthorDto ToDtoWithoutAuthor(Course course) {
        return modelMapper.map(course, CourseWithoutAuthorDto.class);
    }

    public LectureWithoutAuthorDto ToDtoWithoutAuthor(Lecture lecture) {
        return modelMapper.map(lecture, LectureWithoutAuthorDto.class);
    }

    public static List<CourseWithoutAuthorDto> CourseWithoutAuthorListDto(List<Course> courses) {
        return courses.stream().map(DtoMapper::ToDtoWithoutAuthor).collect(Collectors.toList());
    }

    public static List<LectureWithoutAuthorDto> LectureWithoutAuthorListDto(List<Lecture> lectures) {
        return lectures.stream().map(DtoMapper::ToDtoWithoutAuthor).collect(Collectors.toList());
    }

    public CourseDto ToDto(Course course) {
        return modelMapper.map(course, CourseDto.class);
    }

    public LectureDto ToDto(Lecture lecture) {
        return modelMapper.map(lecture, LectureDto.class);
    }

    public static List<DocumentDto> DocumentListDTO(List<Document> documents) {
        return documents.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

    public static DocumentDto ToDto(Document document) {
        return modelMapper.map(document, DocumentDto.class);
    }

    public List<ContentDto> ContentListDto(List<Content> contents) {
        return contents.stream().map(DtoMapper::ToDto).collect(Collectors.toList());
    }

    public ContentDto ToDto(Content content) {
        ContentDto contentDto = modelMapper.map(content, ContentDto.class);
        contentDto.setDocuments(new HashSet<>());
        if (content.getDocuments() != null) {
            for (Document document : content.getDocuments()) {
                contentDto.getDocuments().add(DtoMapper.ToDto(document));
            }
        }
        return contentDto;

    }

    public List<LightContentDto> LightContentListDto(List<Content> contents) {
        return contents.stream().map(DtoMapper::ToLightContentDto).collect(Collectors.toList());
    }

    public LightContentDto ToLightContentDto(Content content) {
        LightContentDto lightContentDto = modelMapper.map(content, LightContentDto.class);

        return lightContentDto;

    }

    public ContentDto toContentDto(Content content, boolean premiumUser) {
        if (content == null) {
            return null;
        }

        ContentDto dto = new ContentDto();
        dto.setContentId(content.getContentId());
        dto.setTitle(content.getTitle());
        dto.setDescription(content.getDescription());
        dto.setLength(content.getLength());
        dto.setFree(content.isFree());
        dto.setCreationTimeDate(content.getCreationTimeDate());
        dto.setVisibility(content.getVisibility());
        dto.setType(content.getType());
        dto.setOrder(content.getOrder());
        dto.setCourseId(content.getCourseId());

        // Dynamically handle video visibility
        if (content.isFree() || premiumUser || content.getVisibility() == Visibility.PUBLIC) {
            dto.setVideoId(content.getVideoId());
        } else {
            dto.setVideoId(""); // hide restricted video
        }

        // If course is eager-fetched, map the author ID safely
        if (content.getCourse() != null) {
            dto.setCourseAuthorId(content.getCourse().getAuthorId());
        }

        // Map documents (if present)
        Set<Document> documents = content.getDocuments();
        if (documents != null && !documents.isEmpty()) {
            dto.setDocuments(
                    documents.stream()
                            .map(DtoMapper::toDocumentDto)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    public List<ContentDto> toContentDtoList(List<Content> contents, boolean premiumUser) {
        return contents.stream()
                .map(content -> toContentDto(content, premiumUser))
                .collect(Collectors.toList());
    }

    private DocumentDto toDocumentDto(Document document) {
        if (document == null) return null;

        DocumentDto dto = new DocumentDto();
        dto.setDocumentId(document.getDocumentId());
        dto.setDocumentName(document.getDocumentName());
        dto.setContentId(document.getContentId());
        return dto;
    }
}
