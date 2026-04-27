package com.medilink.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medilink.identity.entity.DoctorProfile;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    
}
