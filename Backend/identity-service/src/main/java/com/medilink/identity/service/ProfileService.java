package com.medilink.identity.service;

import com.medilink.identity.dto.CreateDoctorProfileRequest;
import com.medilink.identity.dto.CreatePatientProfileRequest;
import com.medilink.identity.entity.DoctorProfile;
import com.medilink.identity.entity.PatientProfile;
import com.medilink.identity.entity.Role;
import com.medilink.identity.entity.User;
import com.medilink.identity.repository.DoctorProfileRepository;
import com.medilink.identity.repository.PatientProfileRepository;
import com.medilink.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorRepo;
    private final PatientProfileRepository patientRepo;

    public DoctorProfile createDoctorProfile(String email, CreateDoctorProfileRequest req) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DoctorProfile profile = DoctorProfile.builder()
                .specialization(req.getSpecialization())
                .experience(req.getExperience())
                .fee(req.getFee())
                .qualification(req.getQualification())
                .licenseNumber(req.getLicenseNumber())
                .clinicName(req.getClinicName())
                .clinicAddress(req.getClinicAddress())
                .bio(req.getBio())
                .user(user)
                .build();

        return doctorRepo.save(profile);
    }

    public PatientProfile createPatientProfile(String email, CreatePatientProfileRequest req) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.PATIENT) {
            throw new RuntimeException("Only patients can create patient profile");
        }

        PatientProfile profile = PatientProfile.builder()
                .age(req.getAge())
                .gender(req.getGender())
                .phone(req.getPhone())
                .address(req.getAddress())
                .bloodGroup(req.getBloodGroup())
                .medicalHistory(req.getMedicalHistory())
                .user(user)
                .build();

        return patientRepo.save(profile);
    }

}
