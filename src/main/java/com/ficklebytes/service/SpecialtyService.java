package com.ficklebytes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ficklebytes.entity.Specialty;
import com.ficklebytes.repository.SpecialtyRepository;
import com.ficklebytes.response.APIResponse;

@Service
public class SpecialtyService {

	@Autowired
	private SpecialtyRepository specialtyRepository;

	public List<Specialty> getAll() {
		return (List<Specialty>) specialtyRepository.findAll();
	}

	public Specialty add(Specialty specialty) {
		return specialtyRepository.save(specialty);
	}

	public APIResponse deleteSpecialty(int spId) {
		APIResponse apiResponse = new APIResponse();
		try {
			boolean exist = specialtyRepository.existsById(spId);
			if (exist) {
				specialtyRepository.deleteById(spId);
				apiResponse.setAll(404, "Specialty deleted successfully", null);
			} else {
				apiResponse.setAll(404, "Specialty not found", null);
			}
		} catch (Exception e) {
			apiResponse.setAll(404, e.getMessage(), null);
		}
		return apiResponse;
	}

}
