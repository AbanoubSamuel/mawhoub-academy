package com.ficklebytes.service;


import com.ficklebytes.entity.Lecture;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.exception.LectureNotFoundException;
import com.ficklebytes.repository.LectureRepository;

import java.util.ArrayList;
import java.util.List;

import com.ficklebytes.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LectureService {


    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    

    public Lecture GetLectureById(int lectureId) throws LectureNotFoundException {
    	Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        if (lecture == null) {
            throw new LectureNotFoundException("Invalid Lecture Id");
        }
        return lecture;
    }

    public List<Lecture> GetAllLectures() {
        ArrayList<Lecture> lectures = new ArrayList<>();
        for (Lecture lecture : lectureRepository.getAllLectures()) {
            	lecture.setPurchasesCount(purchaseRepository.countAllByCourseId(lecture.getCourseId()));
                lectures.add(lecture);
            }
        return lectures;
    }
    
    public List<Lecture> GetAllLecturesByStudyId(int studyId,int fieldId) {
        ArrayList<Lecture> courses = new ArrayList<>();
        for (Lecture course : lectureRepository.findAllByStudyId(studyId)) {
            if (course.getVisibility() == Visibility.PUBLIC && (fieldId == -1
                    || course.getFieldId() == fieldId)) {

                courses.add(course);
            }
        }
        return courses;
        
    }

    public List<Lecture> GetPublicCourses(int fieldId) {
        ArrayList<Lecture> lectures = new ArrayList<>();
        for (Lecture lecture : lectureRepository.getAllLectures()) {
            if (lecture.getVisibility() == Visibility.PUBLIC && (lecture.getStudyId() != null && lecture.getStudyId() != -1) 
            		&& (fieldId == -1 || lecture.getFieldId() == fieldId)) {
            	lectures.add(lecture);
            }
        }
        return lectures;
    }
    
    public List<Lecture> GetPublicLectures(int fieldId) {
        ArrayList<Lecture> lectures = new ArrayList<>();
        for (Lecture lecture : lectureRepository.getAllLectures()) {
            if (lecture.getVisibility() == Visibility.PUBLIC && (lecture.getStudyId() != null && lecture.getStudyId() != -1) 
            		&& (fieldId == -1 || lecture.getFieldId() == fieldId)) {
            	lectures.add(lecture);
            }
        }
        return lectures;
    }

    public List<Lecture> GetLectureLectures(int userId) {
        ArrayList<Lecture> lectures = new ArrayList<>();
        for (Lecture lecture : lectureRepository.getAllByAuthorId(userId)) {
            	lecture.setPurchasesCount(purchaseRepository.countAllByCourseId(lecture.getCourseId()));
                lectures.add(lecture);
        }
        return lectures;
    }

    public Lecture Save(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

}
