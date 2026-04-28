package com.medilink.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medilink.identity.entity.PatientProfile;
import com.medilink.identity.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, UUID> {
    Optional<PatientProfile> findByUser(User user);
}
