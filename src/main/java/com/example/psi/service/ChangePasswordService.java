package com.example.psi.service;

import com.example.psi.entity.PasswordEntity;
import com.example.psi.model.PasswordResult;
import com.example.psi.repository.UserRepository;
import com.example.psi.request.ChangePasswordRequest;
import com.example.psi.response.ChangePasswordResponse;
import com.example.psi.response.LoginResponse;
import com.example.psi.response.RegistrationResponse;
import com.example.psi.singletone.Storage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ChangePasswordService {

    private final PasswordService passwordService;
    private final UserRepository userRepository;
    private final Sha1Service sha1Service;

    public ChangePasswordService(PasswordService passwordService, UserRepository userRepository, Sha1Service sha1Service) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
        this.sha1Service = sha1Service;
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        if(request.oldPassword().isBlank() || request.newPassword().isBlank()){
            return new ChangePasswordResponse("Old password or new password cannot be blank");
        }
        if(request.oldPassword().equals(request.newPassword())) {
            return new ChangePasswordResponse("Old password cannot be equal new password");
        }

        PasswordResult passwordResult = passwordService.countStrangeOfPassword(request.newPassword());
        if (!passwordResult.isOk()) {
            return new ChangePasswordResponse("New " + passwordResult.getError());
        }

        final AtomicReference<ChangePasswordResponse> changePasswordResponseAtomicReference = new AtomicReference<>();
        String email = Storage.getInstance().getCurrentEmail();
        userRepository.findByEmail(email).ifPresent(userEntity -> {
            if(userEntity.isLock()){
                changePasswordResponseAtomicReference.set(new ChangePasswordResponse("Cannot change password fo lock account"));
                return;
            }
            String oldPasswordSha1 = sha1Service.sha1(request.oldPassword());
            if(!userEntity.getCurrentPasswordSha1().equals(oldPasswordSha1)) {
                changePasswordResponseAtomicReference.set(new ChangePasswordResponse("Wrong old password"));
                return;
            }
            String newPasswordSha1 = sha1Service.sha1(request.newPassword());
            userEntity.setTimeOfLastChangedPassword(LocalDateTime.now());
            userEntity.setCurrentPasswordSha1(newPasswordSha1);
            PasswordEntity passwordEntity = new PasswordEntity();
            passwordEntity.setPasswordSha1(newPasswordSha1);
            passwordEntity.setTimestamp(LocalDateTime.now());
            userEntity.addPasswordEntity(passwordEntity);
            userRepository.save(userEntity);
            changePasswordResponseAtomicReference.set(new ChangePasswordResponse(true));
        });
        return changePasswordResponseAtomicReference.get();
    }
}
