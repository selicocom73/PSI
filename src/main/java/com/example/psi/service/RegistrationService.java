package com.example.psi.service;

import com.example.psi.entity.PasswordEntity;
import com.example.psi.entity.UserEntity;
import com.example.psi.model.PasswordResult;
import com.example.psi.repository.PasswordRepository;
import com.example.psi.repository.UserRepository;
import com.example.psi.request.RegisterRequest;
import com.example.psi.response.RegistrationResponse;
import com.example.psi.response.Response;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    private final PasswordService passwordService;

    private final PasswordRepository passwordRepository;

    private final EmailValidator emailValidator;

    private final Sha1Service sha1Service;


    public RegistrationService(UserRepository userRepository,
                               PasswordService passwordService,
                               PasswordRepository passwordRepository,
                               EmailValidator emailValidator,
                               Sha1Service sha1Service) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.passwordRepository = passwordRepository;
        this.emailValidator = emailValidator;
        this.sha1Service = sha1Service;
    }

    public Response register(RegisterRequest request) {

        if (request.email().isBlank() || request.password().isBlank()) {
            return new RegistrationResponse("Cannot register user by empty email or password");
        }
        if (!emailValidator.isValidEmail(request.email())) {
            return new RegistrationResponse("Cannot register user by invalid email");
        }
        boolean exist = userRepository.existsByEmail(request.email());
        if (exist) {
            return new RegistrationResponse("Cannot register user by the same email address");
        }
        PasswordResult passwordResult = passwordService.countStrangeOfPassword(request.password());
        if (!passwordResult.isOk()) {
            return new RegistrationResponse(passwordResult.getError());
        }
        String passwordSha1 = sha1Service.sha1(request.password());
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setPasswordSha1(passwordSha1);
        passwordEntity.setTimestamp(LocalDateTime.now());
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(request.email());
        userEntity.setCurrentPasswordSha1(passwordSha1);
        userEntity.setTimeOfLastChangedPassword(LocalDateTime.now());
        userEntity.addPasswordEntity(passwordEntity);
        userRepository.save(userEntity);
        return new RegistrationResponse(true);
    }
}
