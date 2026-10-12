package com.ficklebytes.repository;


import com.ficklebytes.dto.CourseDto;
import com.ficklebytes.entity.Course;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository("CourseRepository" )
public interface CourseRepository extends CrudRepository<Course, Integer> {

    List<Course> findAllBySchoolId(int schoolId);

    @Cacheable(value = "coursesBySchoolAndField", key = "#schoolId + '-' + #fieldId" )
    @Query("SELECT c FROM Course c " + "LEFT JOIN FETCH c.author " + "LEFT JOIN FETCH c.creator " + "WHERE c.schoolId = :schoolId " + "AND c.visibility = 1 " + "AND (:fieldId = -1 OR c.fieldId = :fieldId)" )
    List<Course> findBySchoolIdAndFieldWithRelations(@Param("schoolId" ) int schoolId, @Param("fieldId" ) int fieldId);


    @Query(value = "SELECT * FROM course c WHERE c.visibility != 2 and c.school_id = :schoolId", nativeQuery = true)
    List<Course> findNonDeletedCoursesBySchoolId(int schoolId);

//    @Query("SELECT new com.ficklebytes.dto.CourseDto(c.id, c.name, c.duration, c.fieldId) " +
//            " FROM Course c " +
//            " WHERE c.school.id = :schoolId " +
//            " AND c.visibility = :visibility " +
//            " AND (:fieldId = -1 OR c.fieldId = :fieldId)")
//    List<CourseDto> findCourseDtos(@Param("schoolId") int schoolId,
//                                   @Param("visibility") Visibility visibility,
//                                   @Param("fieldId") int fieldId);


    @Query(value = "SELECT * FROM course c WHERE c.visibility != 2 and c.school_id != -1", nativeQuery = true)
    List<Course> getAllCourses();

    @Query("SELECT c, a, cr, COUNT(p) " +
            "FROM Course c " +
            "LEFT JOIN c.author a " +
            "LEFT JOIN c.creator cr " +
            "LEFT JOIN Purchase p ON p.courseId = c.courseId AND p.deleted = false " +
            "WHERE c.visibility <> com.ficklebytes.enums.Visibility.DELETED " +
            "AND c.schoolId <> -1 " +
            "GROUP BY c, a, cr")
    List<Object[]> getAllCoursesRaw();

    @Query(value = "SELECT * FROM course c WHERE c.visibility = 1 and c.author_id=:authorId", nativeQuery = true)
    List<Course> getAllByAuthorId(int authorId);

}
