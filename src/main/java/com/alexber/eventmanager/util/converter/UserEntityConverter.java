package com.alexber.eventmanager.util.converter;

import com.alexber.eventmanager.entity.user.User;
import com.alexber.eventmanager.entity.user.UserDto;
import com.alexber.eventmanager.entity.user.UserEntity;
import com.alexber.eventmanager.kafka.entity.Visitor;
import org.springframework.stereotype.Component;

@Component
public class UserEntityConverter {

    public UserEntity toEntity(User user, String encodedPassword) {
        return new UserEntity(
                user.id(),
                user.login(),
                encodedPassword,
                user.age(),
                user.role()
        );
    }

    public User toUser(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.getAge(),
                userEntity.getRole()
        );
    }

    //Сущность для кафки по заданию нужен "Список подписчиков данного мероприятия"
    public Visitor toVisitor(UserEntity userEntity) {
        return new Visitor(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge()
        );
    }
}
