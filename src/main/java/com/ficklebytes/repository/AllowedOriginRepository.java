package com.ficklebytes.repository;

import com.ficklebytes.entity.AllowedOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowedOriginRepository extends JpaRepository<AllowedOrigin, Long> {
}