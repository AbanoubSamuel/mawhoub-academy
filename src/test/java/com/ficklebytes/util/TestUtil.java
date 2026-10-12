package com.ficklebytes.util;

import com.ficklebytes.entity.Card;
import com.ficklebytes.request.purchase.ActivateCardRequest;

import java.time.LocalDateTime;
import java.util.Date;

public class TestUtil {

/*
    public static UserDto GenerateAdminUser(){
        UserDto userDto = new UserDto();
        userDto.setRole(AcademyUser.ADMIN);
        userDto.setUserId(1);
        userDto.setEmail("admin@mawhoubacademy.com");
        userDto.setFullName("Admin");
        return userDto;
    }
    public static AccessRequest GenerateAccessRequest(){
        return new AccessRequest("111","111");
    }
    public static Course GenerateRandomCourse(){
        Course course = new Course();
        course.setAuthorId(1);
        course.setVisibility(utils.GenerateRandomNumber(0,2));
        course.setDescription(utils.GenerateLowercaseString(10));
        course.setCreationTimeDate(LocalDateTime.now());
        course.setCategory("math");
        course.setName(utils.GenerateLowercaseString(7));
        course.setCourseId(utils.GenerateRandomNumber(1,10));
        return course;
    }

    public static Content GenerateRandomContent(){
        return utils.GenerateRandomBoolean() ? GenerateRandomVideoContent() :
         GenerateRandomHeaderContent();
    }
    public static Content GenerateRandomVideoContent(){
        Content content = new Content();
        content.setContentId(utils.GenerateRandomNumber(1,10));
        content.setTitle(utils.GenerateLowercaseString(10));
        content.setCourseId(utils.GenerateRandomNumber(1,10));
        content.setOrder(utils.GenerateRandomNumber(1,10));
        content.setVideoId(utils.GenerateLowercaseString(10));
        content.setFree(utils.GenerateRandomBoolean());
        content.setLength(utils.GenerateRandomNumber(1,10));
        content.setVisibility(utils.GenerateRandomNumber(0,2));
        content.setType(Content.TYPE_VIDEO);
        content.setDescription(utils.GenerateLowercaseString(10));
        content.setCreationTimeDate(LocalDateTime.now());
        return content;
    }

    public static Content GenerateRandomHeaderContent(){
        Content content = new Content();
        content.setContentId(utils.GenerateRandomNumber(1,10));
        content.setTitle(utils.GenerateLowercaseString(10));
        content.setCourseId(utils.GenerateRandomNumber(1,10));
        content.setOrder(utils.GenerateRandomNumber(1,10));
        content.setFree(utils.GenerateRandomBoolean());
        content.setVisibility(utils.GenerateRandomNumber(0,2));
        content.setType(Content.TYPE_HEADER);
        content.setCreationTimeDate(LocalDateTime.now());
        return content;
    }
*/

     public static ActivateCardRequest generateActivateCardRequest(){
         String cardKey = "1234";
         int courseId = 1;
      return ActivateCardRequest.builder()
                 .cardKey(cardKey)
                 .courseId(courseId)
                 .build();
     }

}
