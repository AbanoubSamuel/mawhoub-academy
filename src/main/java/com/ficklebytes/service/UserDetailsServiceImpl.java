package com.ficklebytes.service;

import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.repository.AcademyUserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AcademyUserRepository academyUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AcademyUser> applicationUser =
                academyUserRepository.findByEmailIgnoreCase(username);
        if (!applicationUser.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return applicationUser.get();
    }
}
