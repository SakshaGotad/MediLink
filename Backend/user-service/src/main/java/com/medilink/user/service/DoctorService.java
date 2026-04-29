package com.medilink.user.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.medilink.user.dto.DoctorResponse;
import com.medilink.user.entity.DoctorProfile;
import com.medilink.user.repository.DoctorProfileRepository;

@Service
public class DoctorService {
    private final DoctorProfileRepository doctorRepo;

    public DoctorService(DoctorProfileRepository doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public List<DoctorResponse> getAllDoctors(String specialization, String sort){
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

        DoctorProfile doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return mapToResponse(doctor);
    }


    private DoctorResponse mapToResponse(DoctorProfile profile) {
        return DoctorResponse.builder()
                .id(profile.getId())
                .specialization(profile.getSpecialization())
                .experience(profile.getExperience())
                .fee(profile.getFee())
                .qualification(profile.getQualification())
                .clinicName(profile.getClinicName())
                .build();
    }
}
