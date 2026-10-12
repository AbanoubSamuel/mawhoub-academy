package com.ficklebytes.repository;

import com.ficklebytes.entity.Content;
import com.ficklebytes.enums.Visibility;

import java.util.Set;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository("ContentRepository")
public interface ContentRepository extends CrudRepository<Content, Integer> {

    List<Content> findAllByCourseIdOrderByOrder(int courseId);

    @Query(
            " SELECT c " +
                    " FROM Content c " +
                    " WHERE c.courseId = :courseId " +
                    " AND c.visibility IN :visibilities " +
                    " ORDER BY c.order ASC "
    )
    List<Content> findVisibleByCourseId(
            @Param("courseId") int courseId,
            @Param("visibilities") Set<Visibility> visibilities);

    @Query("SELECT DISTINCT c " +
                " FROM Content c " +
                " LEFT JOIN FETCH c.documents d " +
                " WHERE c.courseId = :courseId " +
                  " AND c.visibility IN :visibilities " +
                " ORDER BY c.order ASC ")
    List<Content> findAllByCourseIdWithDocumentsAndVisibility(
            @Param("courseId") int courseId,
            @Param("visibilities") Set<Visibility> visibilities);


    List<Content> findAllByVisibilityEqualsAndCourseIdEqualsOrderByOrder(Visibility visibility,
                                                                         int courseId);

    int countByCourseId(int courseId);
}
