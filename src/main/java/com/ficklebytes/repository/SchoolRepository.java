
package com.ficklebytes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ficklebytes.entity.School;

import org.springframework.transaction.annotation.Transactional;


@Repository("SchoolRepository")
public interface SchoolRepository extends JpaRepository<School, Integer>{


	@Query("SELECT s FROM School s WHERE s.id != -1 AND s.visibility = 1")
	List<School> findByVisibility();

	@Query("SELECT s FROM School s WHERE s.id = :schoolId AND s.visibility = 1")
	List<School> findByVisibilityAndId(int schoolId);

	@Modifying
	@Transactional
	@Query("UPDATE School s SET s.visibility = 0 WHERE s.id = :schoolId")
	int hiddenVisibility(int schoolId);


}
