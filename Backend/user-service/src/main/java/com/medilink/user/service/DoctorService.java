package com.medilink.user.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.medilink.user.dto.DoctorResponse;
import com.medilink.user.entity.DoctorProfile;
import com.medilink.user.exception.ResourceNotFoundException;
import com.medilink.user.repository.DoctorProfileRepository;
import com.medilink.user.repository.PatientProfileRepository;
import com.medilink.user.dto.PatientResponse;
import com.medilink.user.entity.PatientProfile;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DoctorService {
    private final DoctorProfileRepository doctorRepo;
    private final PatientProfileRepository patientRepo;
    private final RestTemplate restTemplate;

    public DoctorService(DoctorProfileRepository doctorRepo, PatientProfileRepository patientRepo, RestTemplate restTemplate) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.restTemplate = restTemplate;
    }

    public List<DoctorResponse> getAllDoctors(String specialization, String sort){
        log.info("Fetching all doctors with specialization: {} and sort: {}", specialization, sort);
        List<DoctorProfile> doctors;
    
        // filter 
        if (specialization != null) {
            doctors = doctorRepo.findBySpecializationIgnoreCase(specialization);
        } else {
            doctors = doctorRepo.findAll();
        }

        //sort
        if ("fee".equalsIgnoreCase(sort)) {
            doctors.sort(Comparator.comparing(DoctorProfile::getFee));
        } else if ("experience".equalsIgnoreCase(sort)) {
            doctors.sort(Comparator.comparing(DoctorProfile::getExperience).reversed());
        }
        return doctors.stream()
                .map(this::mapToResponse)
                .toList();
    }

      public DoctorResponse getDoctorById(UUID id) {
        log.info("Fetching doctor profile with id: {}", id);

        DoctorProfile doctor = doctorRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Doctor profile not found for id: {}", id);
                    return new ResourceNotFoundException("Doctor not found  " );
                });

        return mapToResponse(doctor);
    }

    public List<PatientResponse> getPatientsForDoctor(UUID doctorId) {
        log.info("Fetching patients for doctor id: {}", doctorId);
        
        // 1. Call appointment-service to get unique patient IDs
        String url = "http://localhost:8083/appointments/doctor/" + doctorId + "/patient-ids";
        try {
            UUID[] patientIdsArray = restTemplate.getForObject(url, UUID[].class);
            if (patientIdsArray == null || patientIdsArray.length == 0) {
                return Collections.emptyList();
            }

            List<UUID> patientIds = List.of(patientIdsArray);

            // 2. Fetch patient profiles from database
            List<PatientProfile> patients = patientRepo.findAllById(patientIds);

            return patients.stream()
                    .map(this::mapToPatientResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching patients from appointment-service", e);
            return Collections.emptyList();
        }
    }

    private PatientResponse mapToPatientResponse(PatientProfile profile) {
        return PatientResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .email(profile.getEmail())
                .age(profile.getAge())
                .gender(profile.getGender())
                .phone(profile.getPhone())
                .bloodGroup(profile.getBloodGroup())
                .medicalHistory(profile.getMedicalHistory())
                .allergies(profile.getAllergies())
                .build();
    }


    private DoctorResponse mapToResponse(DoctorProfile profile) {
        return DoctorResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .email(profile.getEmail())
                .specialization(profile.getSpecialization())
                .experience(profile.getExperience())
                .fee(profile.getFee())
                .qualification(profile.getQualification())
                .clinicName(profile.getClinicName())
                .build();
    }
}
