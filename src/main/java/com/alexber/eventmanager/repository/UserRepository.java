package com.alexber.eventmanager.repository;

import com.alexber.eventmanager.entity.user.UserEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(@NotBlank String login);
    boolean existsByLogin(@NotBlank String login);
}
