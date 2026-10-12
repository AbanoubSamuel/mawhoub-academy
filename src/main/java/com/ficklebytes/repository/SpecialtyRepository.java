package com.ficklebytes.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ficklebytes.entity.Specialty;

@Repository("SpecialtyRepository")
public interface SpecialtyRepository extends CrudRepository<Specialty, Integer> {

}

