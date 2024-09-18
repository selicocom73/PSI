package com.example.psi.service;

import com.example.psi.entity.PasswordEntity;
import com.example.psi.entity.UserEntity;
import com.example.psi.repository.PasswordRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PasswordHistoryService {

    private final PasswordRepository passwordRepository;

    public PasswordHistoryService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public boolean canAcceptNewPasswordByHistoryPasswords(UserEntity userEntity, String newPasswordSha1) {
        Pageable pageable = PageRequest.of(0, 5);
        List<PasswordEntity> oldPasswordEntities = passwordRepository.findLatest5PasswordsByUser(userEntity, pageable);
        Set<String> oldPasswordsSha1 = oldPasswordEntities.stream().map(PasswordEntity::getPasswordSha1).collect(Collectors.toSet());
        return !oldPasswordsSha1.contains(newPasswordSha1);
    }
}
