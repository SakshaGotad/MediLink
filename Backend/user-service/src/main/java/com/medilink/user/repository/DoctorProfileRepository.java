package com.medilink.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medilink.user.entity.DoctorProfile;
import com.medilink.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, UUID> {
    Optional<DoctorProfile> findByUser(User user);
    List<DoctorProfile> findBySpecializationIgnoreCase(String specialization);
}
