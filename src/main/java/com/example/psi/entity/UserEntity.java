package com.example.psi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String codeToChangePassword;

    private String currentPasswordSha1;

    private boolean logged;
    private boolean lock;
    private LocalDateTime timeOfLocked;

    private LocalDateTime timeOfLastChangedPassword;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PasswordEntity> passwordEntities;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<InvalidLoginEntity> invalidLoginEntities;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SessionEntity> sessionEntities;


    public void addPasswordEntity(PasswordEntity passwordEntity) {
        if(passwordEntities == null) {
            passwordEntities = new ArrayList<>();
        }
        passwordEntity.setUser(this);
        passwordEntities.add(passwordEntity);
    }

    public void addInvalidLoginEntity(InvalidLoginEntity invalidLoginEntity) {
        if(invalidLoginEntities == null) {
            invalidLoginEntities = new ArrayList<>();
        }
        invalidLoginEntity.setUser(this);
        invalidLoginEntities.add(invalidLoginEntity);
    }

    public void addSessionEntity(SessionEntity sessionEntity) {
        if(sessionEntities == null) {
            sessionEntities = new ArrayList<>();
        }
        sessionEntity.setUser(this);
        sessionEntities.add(sessionEntity);
    }
}
