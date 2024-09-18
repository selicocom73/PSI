package com.example.psi.service;

import com.example.psi.entity.PasswordEntity;
import com.example.psi.model.PasswordResult;
import com.example.psi.repository.UserRepository;
import com.example.psi.request.ChangePasswordRequest;
import com.example.psi.request.CreateNewPasswordRequest;
import com.example.psi.response.ChangePasswordResponse;
import com.example.psi.response.CreateNewPasswordResponse;
import com.example.psi.singletone.Storage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CreateNewPasswordService {

    private final PasswordService passwordService;
    private final UserRepository userRepository;

    private final PasswordHistoryService passwordHistoryService;
    private final Sha1Service sha1Service;

    public CreateNewPasswordService(PasswordService passwordService, UserRepository userRepository, PasswordHistoryService passwordHistoryService, Sha1Service sha1Service) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
        this.passwordHistoryService = passwordHistoryService;
        this.sha1Service = sha1Service;
    }

    public CreateNewPasswordResponse changePassword(CreateNewPasswordRequest request) {
        if(request.newPassword().isBlank()){
            return new CreateNewPasswordResponse("Old password or new password cannot be blank");
        }

        PasswordResult passwordResult = passwordService.countStrangeOfPassword(request.newPassword());
        if (!passwordResult.isOk()) {
            return new CreateNewPasswordResponse("New " + passwordResult.getError());
        }

        final AtomicReference<CreateNewPasswordResponse> changePasswordResponseAtomicReference = new AtomicReference<>();
        changePasswordResponseAtomicReference.set(new CreateNewPasswordResponse(true));
        String email = Storage.getInstance().getCurrentEmail();
        userRepository.findByEmail(email).ifPresent(userEntity -> {
            if(userEntity.isLock()){
                changePasswordResponseAtomicReference.set(new CreateNewPasswordResponse("Cannot change password fo lock account"));
                return;
            }
            String newPasswordSha1 = sha1Service.sha1(request.newPassword());
            boolean newPasswordIsAccepted = passwordHistoryService.canAcceptNewPasswordByHistoryPasswords(userEntity, newPasswordSha1);
            if (newPasswordIsAccepted) {
                userEntity.setTimeOfLastChangedPassword(LocalDateTime.now());
                userEntity.setCurrentPasswordSha1(newPasswordSha1);
                PasswordEntity passwordEntity = new PasswordEntity();
                passwordEntity.setPasswordSha1(newPasswordSha1);
                userEntity.addPasswordEntity(passwordEntity);
                userRepository.save(userEntity);
                changePasswordResponseAtomicReference.set(new CreateNewPasswordResponse(true));
            } else {
                changePasswordResponseAtomicReference.set(new CreateNewPasswordResponse("Cannot change password is equal to one from old passwords"));
            }
        });
        return changePasswordResponseAtomicReference.get();
    }
}
