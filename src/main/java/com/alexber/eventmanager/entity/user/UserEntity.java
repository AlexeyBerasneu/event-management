package com.alexber.eventmanager.entity.user;

import com.alexber.eventmanager.entity.registration.RegistrationEntity;
import com.alexber.eventmanager.exception.customexception.AmountRegistrationException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotNull
    @Min(18)
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RegistrationEntity> registrations = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(Long id, String login, String password, Integer age, UserRole role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.age = age;
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<RegistrationEntity> getRegistrations() {
        return registrations;
    }

    public void addRegistration(RegistrationEntity registration) {
        registrations.add(registration);
        registration.setUser(this);
    }

    public void removeRegistration(RegistrationEntity registration) {
        if (!registrations.contains(registration)) {
            throw new AmountRegistrationException("User does not have any registrations");
        }
        registrations.remove(registration);
    }
}
