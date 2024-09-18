package com.example.psi.service;

import com.example.psi.entity.InvalidLoginEntity;
import com.example.psi.entity.SessionEntity;
import com.example.psi.repository.InvalidLoginRepository;
import com.example.psi.repository.PasswordRepository;
import com.example.psi.repository.UserRepository;
import com.example.psi.request.LoginRequest;
import com.example.psi.response.LoginResponse;
import com.example.psi.singletone.Storage;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class LoginService {

    private static final Duration LOCK_DURATION = Duration.ofMinutes(5L);
    private static final int NUMBER_OF_INVALID_LOGINS_TO_LOCK_USER = 3;
    private static final Duration DURATION_FOR_N_INVALID_LOGINS_PER_EMAIL_TO_LOCK_USER = Duration.ofMinutes(1);

    private final UserRepository userRepository;
    private final Sha1Service sha1Service;


    public LoginService(UserRepository userRepository, Sha1Service sha1Service) {
        this.userRepository = userRepository;
        this.sha1Service = sha1Service;
    }

    public LoginResponse login(LoginRequest request) {
        if (request.email().isBlank() || request.password().isBlank()) {
            return new LoginResponse("Invalid login or password");
        }
        boolean existUserByEmail = userRepository.existsByEmail(request.email());
        final AtomicReference<LoginResponse> loginResponseAtomicReference = new AtomicReference<>();
        if (existUserByEmail) {
            userRepository.findByEmail(request.email()).ifPresent(userEntity -> {
                LocalDateTime now = LocalDateTime.now();
                // Konto zablokowane
                if (userEntity.isLock() && now.minus(LOCK_DURATION).isBefore(userEntity.getTimeOfLocked())) {
                    long numberOfMinutesToUnlock = Duration.between(userEntity.getTimeOfLocked(), now).toMinutes();
                    loginResponseAtomicReference.set(new LoginResponse("Account blocked for " + numberOfMinutesToUnlock + "minutes"));
                    return;
                }
                // Konto odblokowane
                userEntity.setLock(false);
                userEntity.setTimeOfLocked(null);

                //Logowanie poprawne
                String sha1PasswordFromDb = userEntity.getCurrentPasswordSha1();
                String sha1PasswordFromView = sha1Service.sha1(request.password());
                if (sha1PasswordFromDb.equals(sha1PasswordFromView)) {
                    userEntity.setLogged(true);
                    SessionEntity sessionEntity = new SessionEntity();
                    sessionEntity.setStart(LocalDateTime.now());
                    userEntity.addSessionEntity(sessionEntity);
                    userRepository.save(userEntity);
                    Storage.getInstance().setCurrentEmail(request.email());
                    loginResponseAtomicReference.set(new LoginResponse(true));
                    return;
                }
                long numberInvalidLoginsInLastTime = userEntity.getInvalidLoginEntities().stream()
                        .filter(f->f.getTimestamp().isAfter(LocalDateTime.now().minus(DURATION_FOR_N_INVALID_LOGINS_PER_EMAIL_TO_LOCK_USER))).count();
                if(numberInvalidLoginsInLastTime >= NUMBER_OF_INVALID_LOGINS_TO_LOCK_USER) {
                    userEntity.setLock(true);
                    userEntity.setTimeOfLocked(LocalDateTime.now());
                }
                // Logowanie niepoprawne
                InvalidLoginEntity invalidLoginEntity = new InvalidLoginEntity();
                invalidLoginEntity.setTimestamp(LocalDateTime.now());
                userEntity.addInvalidLoginEntity(invalidLoginEntity);
                userRepository.save(userEntity);
                loginResponseAtomicReference.set(new LoginResponse("Invalid login or password"));
            });
            return loginResponseAtomicReference.get();
        }
        return new LoginResponse("Invalid login or password");
    }
}
