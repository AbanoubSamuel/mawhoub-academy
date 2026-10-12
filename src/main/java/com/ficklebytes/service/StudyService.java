package com.ficklebytes.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ficklebytes.entity.Lecture;
import com.ficklebytes.entity.Study;
import com.ficklebytes.repository.CourseRepository;
import com.ficklebytes.repository.LectureRepository;
import com.ficklebytes.repository.StudyRepository;
import com.ficklebytes.response.APIResponse;

@Service
public class StudyService {

	  @Autowired
	  private StudyRepository studyRepository;
	  
	  @Autowired
	  private LectureRepository lectureRepository;

	  @Autowired
	  private ModelMapper modelMapper;
	  
	  public List<Study> getStudies() {
	        return (List<Study>) studyRepository.findByIdNot(-1);
	  }
	  
	  public APIResponse  deleteStudy(int studyId) {
		  APIResponse apiResponse = new APIResponse();
	      
		  try {
	          boolean exist = studyRepository.existsById(studyId);
	          if (exist) {
	        	  List<Lecture> lectures = lectureRepository.findAllByStudyId(studyId);
	        	  
	        	  if(lectures != null && lectures.size() > 0) {
	        		  String message = "Unable to delete this study because it contains " + lectures.size() + " lecture(s). "
	        		  		+ "Please remove all associated lectures before attempting to delete the study.";
	        		  apiResponse.setAll(404, message, null);
	        	  } else {
	        		  studyRepository.deleteById(studyId);
	        		  apiResponse.setAll(404, "Study deleted successfully", null);	        		  
	        	  }
	          } else {
	              apiResponse.setAll(404, "Study not found", null);
	          }
	      } catch (Exception e) {
	          apiResponse.setAll(404, e.getMessage(), null);
	      }
	      return apiResponse;
	  }
	  
	  public Study  addStudy(Study study) {	 
		  System.out.println("Study ::" + study.toString());
		  return  studyRepository.save(study);

	  }
}
