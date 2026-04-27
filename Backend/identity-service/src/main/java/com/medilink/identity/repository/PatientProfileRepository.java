package com.medilink.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medilink.identity.entity.PatientProfile;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
    
}
