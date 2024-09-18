package com.example.psi.repository;

import com.example.psi.entity.InvalidLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidLoginRepository extends JpaRepository<InvalidLoginEntity, Long> {
}
