package com.example.psi.repository;

import com.example.psi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    boolean existsByEmail(String email);
    boolean existsByEmailAndCurrentPasswordSha1(String email, String currentPasswordSha1);

    Optional<UserEntity> findByEmail(String email);
}
