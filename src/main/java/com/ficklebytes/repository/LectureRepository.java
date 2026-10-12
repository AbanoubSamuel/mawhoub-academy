package com.ficklebytes.repository;


import com.ficklebytes.entity.Lecture;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("LectureRepository")
public interface LectureRepository extends CrudRepository<Lecture, Integer> {
        
    List<Lecture> findAllByStudyId(int studyId);
    
    @Query(
    		value = "SELECT * FROM course c WHERE c.visibility != 2 and c.study_id != -1",
    		  nativeQuery = true)
    List<Lecture> getAllLectures();
    
    @Query(
    		value = "SELECT * FROM course c WHERE c.visibility = 1 and c.study_id != -1 and c.author_id=:authorId", 
    		  nativeQuery = true)
      List<Lecture> getAllByAuthorId(int authorId);
    
}
