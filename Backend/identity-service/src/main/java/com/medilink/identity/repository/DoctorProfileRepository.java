package com.medilink.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medilink.identity.entity.DoctorProfile;
import com.medilink.identity.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, UUID> {
    Optional<DoctorProfile> findByUser(User user);
}
