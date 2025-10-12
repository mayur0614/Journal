package com.csmayur.Journal.service;

import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.repository.UserEntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Component // ✅ Prefer @Service for clarity
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserEntryRepo userEntryRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userEntryRepo.findByUserName(username);
        if(user != null){
            UserDetails  userDetails = User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
            return userDetails;
        }
        throw new UsernameNotFoundException("User Not Found with name "+user.getUserName());
    }

   /* @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 Trying to load user: " + username);

        UserEntity user = userEntryRepo.findByUserName(username);

        if (user == null) {
            System.out.println("❌ User not found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Ensure roles are not null or empty
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            System.out.println("⚠️ User has no roles, assigning default USER role.");
            user.setRoles(Arrays.asList("USER"));
        }

        System.out.println("✅ Found user: " + username);
        System.out.println("🧩 Encoded password: " + user.getPassword());
        System.out.println("🔑 Roles: " + user.getRoles());

        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword()) // Already encoded with BCrypt
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }*/
}
