package com.ficklebytes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ficklebytes.entity.Course;
import com.ficklebytes.entity.School;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.repository.CardRepository;
import com.ficklebytes.repository.CourseRepository;
import com.ficklebytes.repository.SchoolRepository;
import com.ficklebytes.response.APIResponse;

@Service
public class SchoolService {

	  @Autowired
	  private SchoolRepository schoolRepository;
	  
	  @Autowired
	  private CourseRepository courseRepository;

	  @Autowired
	  private ModelMapper modelMapper;
	  
	  public List<School> getSchools() {
	        return schoolRepository.findByVisibility();
	  }
	  
	  public APIResponse  deleteSchool(int schoolId) {
		  APIResponse apiResponse = new APIResponse();
	      
		  try {
			  List<School> byVisibilityAndId = schoolRepository.findByVisibilityAndId(schoolId);
			  if (byVisibilityAndId != null && !byVisibilityAndId.isEmpty()) {
	        	  List<Course> courses = courseRepository.findNonDeletedCoursesBySchoolId(schoolId);

	        	  if(courses != null && !courses.isEmpty()) {
	        		  String message = "Unable to delete this school because it contains " + courses.size() + " course(s). "
	        		  		+ "Please remove all associated courses before attempting to delete the school.";
	        		  apiResponse.setAll(404, message, null);
	        	  } else {
	        		  int nRows = schoolRepository.hiddenVisibility(schoolId);
	        		  apiResponse.setAll(404, "School deleted successfully", null);	        		  
	        	  }
	          } else {
	              apiResponse.setAll(404, "School not found", null);
	          }
	      } catch (Exception e) {
	          apiResponse.setAll(404, e.getMessage(), null);
	      }
	      return apiResponse;
	  }
	  
	  public School  addSchool(School school) {	 
		  System.out.println("School ::" + school.toString());
		  return  schoolRepository.save(school);

	  }
}
