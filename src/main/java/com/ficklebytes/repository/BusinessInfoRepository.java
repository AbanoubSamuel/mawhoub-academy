package com.ficklebytes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ficklebytes.entity.BusinessInfo;

@Repository("BusinessInfoRepository")
public interface BusinessInfoRepository extends JpaRepository<BusinessInfo, String> {

}
//public interface SchoolRepository extends JpaRepository<School, Integer>{
	 
