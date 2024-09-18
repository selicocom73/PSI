package com.example.psi.service;

import com.example.psi.entity.SessionEntity;
import com.example.psi.model.InvalidLogin;
import com.example.psi.model.LoggedUser;
import com.example.psi.model.Session;
import com.example.psi.repository.SessionRepository;
import com.example.psi.repository.UserRepository;
import com.example.psi.singletone.Storage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class LoggedUserService {


    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    public LoggedUserService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public LoggedUser getLoggedUser() {
        String email = Storage.getInstance().getCurrentEmail();
        LoggedUser loggedUser = new LoggedUser();
        userRepository.findByEmail(email).ifPresent(userEntity -> {
            loggedUser.setId(userEntity.getId());
            loggedUser.setLogged(true);
            loggedUser.setEmail(email);
            loggedUser.setTimeOfLastChangedPassword(userEntity.getTimeOfLastChangedPassword());
            List<InvalidLogin> invalidLogins = new ArrayList<>();
            userEntity.getInvalidLoginEntities().forEach(invalidLoginEntity -> {
                InvalidLogin invalidLogin = new InvalidLogin();
                invalidLogin.setId(invalidLoginEntity.getId());
                invalidLogin.setLocalDateTime(invalidLoginEntity.getTimestamp());
                invalidLogins.add(invalidLogin);
            });
            loggedUser.setInvalidLogins(invalidLogins);
            List<Session> sessions = new ArrayList<>();
            userEntity.getSessionEntities().forEach(sessionEntity -> {
                Session session = new Session();
                session.setId(sessionEntity.getId());
                session.setStart(sessionEntity.getStart());
                session.setStop(sessionEntity.getStop());
                sessions.add(session);
            });
            loggedUser.setSessions(sessions);
            loggedUser.setLock(userEntity.isLock());
            loggedUser.setTimeOfLocked(userEntity.getTimeOfLocked());
            loggedUser.setLogged(userEntity.isLogged());
        });
        return loggedUser;
    }

    public boolean logout() {
        String email = Storage.getInstance().getCurrentEmail();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        userRepository.findByEmail(email).ifPresent(userEntity -> {
            userEntity.setLogged(false);
            userEntity.getSessionEntities().stream().filter(session -> session.getStop() == null)
                    .map(SessionEntity::getId).findFirst().
                    flatMap(sessionRepository::findById).ifPresent(sessionEntity -> {
                        sessionEntity.setStop(LocalDateTime.now());
                        sessionRepository.save(sessionEntity);
                        atomicBoolean.set(true);
                    });
        });
        return atomicBoolean.get();
    }
}
