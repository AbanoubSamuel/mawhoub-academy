package com.ficklebytes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ficklebytes.entity.Study;

@Repository("StudyRepository")
public interface StudyRepository extends JpaRepository<Study, Integer>{
	 
	 List<Study> findByIdNot(Integer id);
}
