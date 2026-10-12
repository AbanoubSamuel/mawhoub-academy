package com.ficklebytes.repository;


import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.enums.Role;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository("UserRepository")
public interface AcademyUserRepository extends CrudRepository<AcademyUser,
    Integer> {

  Optional<AcademyUser> findByUserId(int userId);

  Optional<AcademyUser> findByEmailIgnoreCase(String email);
  
  List<AcademyUser> findAllByRoleEquals(Role role);
  
  @Transactional
  @Modifying
  @Query("UPDATE AcademyUser u SET u.role = :role WHERE u.userId = :userId")
  int updateUserRole(@Param("userId") int userId, @Param("role") Role role);
  
}
