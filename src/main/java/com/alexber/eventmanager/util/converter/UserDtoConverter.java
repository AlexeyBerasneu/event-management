package com.alexber.eventmanager.util.converter;

import com.alexber.eventmanager.entity.user.*;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public User toUser(UserRegistration userRegistration, UserRole role) {
        return new User(
                null,
                userRegistration.login(),
                userRegistration.password(),
                userRegistration.age(),
                role
        );
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.age(),
                user.role()
        );
    }
}
