package com.alexber.eventmanager.controller;

import com.alexber.eventmanager.entity.user.*;
import com.alexber.eventmanager.security.jwt.JwtAuthenticationService;
import com.alexber.eventmanager.security.jwt.JwtTokenResponse;
import com.alexber.eventmanager.service.UserService;
import com.alexber.eventmanager.util.converter.UserDtoConverter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final String DEFAULT_USER_ROLE = "USER";

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserDtoConverter userDtoConverter;
    private final JwtAuthenticationService jwtAuthenticationService;

    public UserController(UserService userService, UserDtoConverter userDtoConverter, JwtAuthenticationService jwtAuthenticationService) {
        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody
                                                @Valid
                                                UserRegistration userRegistration) {
        logger.info("Registering user: {}", userRegistration);
        User newUser = userService.createUser(userDtoConverter.toUser(userRegistration, UserRole.valueOf(DEFAULT_USER_ROLE)));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.toUserDto(newUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable
                                           @NotNull
                                           @Positive
                                           Long id) {
        logger.info("Getting user: {}", id);
        User foundedUser = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDtoConverter.toUserDto(foundedUser));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticateUser(@Valid @RequestBody UserCredentials userCredentials) {
        logger.info("Authenticating user: {}", userCredentials);
        var token = jwtAuthenticationService.authenticateUser(userCredentials);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtTokenResponse(token));
    }
}
