package com.example.psi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LoggedUser {

    private Long id;

    private String email;

    private boolean logged;
    private boolean lock;
    private LocalDateTime timeOfLocked;

    private LocalDateTime timeOfLastChangedPassword;

    private List<Session> sessions = new ArrayList<>();

    private List<InvalidLogin> invalidLogins = new ArrayList<>();
}
