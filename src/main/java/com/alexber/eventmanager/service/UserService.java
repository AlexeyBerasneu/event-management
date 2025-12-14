package com.alexber.eventmanager.service;

import com.alexber.eventmanager.entity.user.User;
import com.alexber.eventmanager.entity.user.UserEntity;
import com.alexber.eventmanager.repository.UserRepository;
import com.alexber.eventmanager.util.UserEntityConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEntityConverter userEntityConverter;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserEntityConverter userEntityConverter) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userEntityConverter = userEntityConverter;
    }


    public User createUser(User user) {
        if (userRepository.existsByLogin(user.login())) {
            throw new IllegalArgumentException("User with login " + user.login() + " already exists");
        }
        String encodedPassword = passwordEncoder.encode(user.password());
        UserEntity userEntity = userEntityConverter.toEntity(user, encodedPassword);
        UserEntity savedUser = userRepository.save(userEntity);
        return userEntityConverter.toUser(savedUser);
    }

    public User getUserById(Long id) {
        UserEntity foundedUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return userEntityConverter.toUser(foundedUser);
    }

    public User getUserByLogin(String login) {
        UserEntity foundUserENtity = userRepository.findByLogin(login).orElseThrow(() -> new EntityNotFoundException("User with login " + login + " not found"));
        return userEntityConverter.toUser(foundUserENtity);
    }
}
