package com.medilink.user.service;

import com.medilink.user.dto.CreateDoctorProfileRequest;
import com.medilink.user.exception.ProfileAlreadyExistsException;
import com.medilink.user.exception.ResourceNotFoundException;
import com.medilink.user.exception.UnauthorizedAccessException;
import com.medilink.user.dto.CreatePatientProfileRequest;
import com.medilink.user.entity.DoctorProfile;
import com.medilink.user.entity.PatientProfile;
import com.medilink.user.entity.Role;
import com.medilink.user.entity.User;
import com.medilink.user.repository.DoctorProfileRepository;
import com.medilink.user.repository.PatientProfileRepository;
import com.medilink.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorRepo;
    private final PatientProfileRepository patientRepo;

    public DoctorProfile createDoctorProfile(String email, CreateDoctorProfileRequest req) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                 if (doctorRepo.findByUser(user).isPresent()) {
        throw new ProfileAlreadyExistsException("Doctor profile already exists");
    }

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.PATIENT) {
            throw new UnauthorizedAccessException("Only patients can create patient profile");
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

    public PatientProfile getPatientProfile( Authentication auth){
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.PATIENT) {
            throw new UnauthorizedAccessException("Only patients can get patient profile");
        }

        return patientRepo.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
    }

    public DoctorProfile getDoctorProfile(Authentication auth){
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.DOCTOR) {
            throw new UnauthorizedAccessException("Only doctors can get doctor profile");
        }

        return doctorRepo.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
    }

    public DoctorProfile updateDoctorProfile(
        UUID doctorId,
        CreateDoctorProfileRequest request,
        Authentication auth
    ){
        DoctorProfile doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (!doctor.getUser().getEmail().equals(auth.getName())) {
            throw new UnauthorizedAccessException("You are not authorized to update this profile");
        }

        doctor.setSpecialization(request.getSpecialization());
        doctor.setExperience(request.getExperience());
        doctor.setFee(request.getFee());
        doctor.setQualification(request.getQualification());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setClinicName(request.getClinicName());
        doctor.setClinicAddress(request.getClinicAddress());
        doctor.setBio(request.getBio());

        return doctorRepo.save(doctor);
    }

    public PatientProfile updatePatientProfile(
        UUID patientId,
        CreatePatientProfileRequest request,
        Authentication auth
    ){
        PatientProfile patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (!patient.getUser().getEmail().equals(auth.getName())) {
            throw new UnauthorizedAccessException("You are not authorized to update this profile");
        }

        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setAddress(request.getAddress());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setMedicalHistory(request.getMedicalHistory());

        return patientRepo.save(patient);
    }
}
