package com.alexber.eventmanager.security;

import com.alexber.eventmanager.entity.user.UserEntity;
import com.alexber.eventmanager.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByLogin(login).orElseThrow(
                () -> new UsernameNotFoundException("User with username " + login + " not found")
        );
        return User.withUsername(login)
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
    }
}
