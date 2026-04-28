package com.medilink.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medilink.user.entity.PatientProfile;
import com.medilink.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, UUID> {
    Optional<PatientProfile> findByUser(User user);
}
