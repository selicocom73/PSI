package com.example.psi.repository;

import com.example.psi.entity.PasswordEntity;
import com.example.psi.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordEntity, Long> {

    @Query("SELECT p FROM PasswordEntity p WHERE p.user = :user ORDER BY p.timestamp DESC")
    List<PasswordEntity> findLatest5PasswordsByUser(@Param("user") UserEntity user, Pageable pageable);
}
