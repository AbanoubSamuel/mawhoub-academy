package com.ficklebytes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ficklebytes.enums.Role;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class AcademyUser implements Serializable, UserDetails {

  @Column(name = "user_id")
  @Id
  private int userId;

  @Column(name = "token")
  private String token;

  @Column(name = "email")
  private String email;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "biography")
  private String biography;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "role")
  @Enumerated(EnumType.ORDINAL)
  private Role role;

  @Column(name = "validated_email")
  private boolean validated;

  @Column(name = "password")
  @JsonIgnore
  private String password;

  @Column(name = "picture_url")
  private String pictureUrl;

  @Column(name = "creation_date")
  private LocalDateTime creationTimeDate;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
  private Set<Purchase> purchases;
}
