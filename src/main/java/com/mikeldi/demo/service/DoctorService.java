package com.mikeldi.demo.service;

import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {
	
	private final DoctorRepository doctorRepository;

	public DoctorService(DoctorRepository doctorRepository) {
		this.doctorRepository = doctorRepository;
	}
	
	// Obtener doctor por id
    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado"));
    }
    
    //Obtener doctor por Email
    public Doctor findByEmail(String email) {
    	return doctorRepository.findByEmail(email)
    			.orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado"));
    }
    
    // Obtener todos los doctores verificados
    public List<Doctor> findAllVerified() {
        return doctorRepository.findByVerifiedTrue();
    }
 
    // Buscar doctores por nombre
    public List<Doctor> searchByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Buscar doctores por especialidad
    public List<Doctor> findBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty);
    }
    
    // Actualizar perfil del doctor
    @Transactional
    public Doctor updateProfile(Long id, Doctor updated) {
        Doctor doctor = findById(id);
        doctor.setNombre(updated.getNombre());
        doctor.setEspecialidad(updated.getEspecialidad());
        doctor.setPhotoUrl(updated.getPhotoUrl());
        return doctorRepository.save(doctor);
    }
    
    // Verificar un doctor (acción del admin)
    @Transactional
    public Doctor verifyDoctor(Long id) {
        Doctor doctor = findById(id);
        doctor.setVerificado(true);
        return doctorRepository.save(doctor);
    }
 
    // Guardar token de Google Calendar
    @Transactional
    public void saveGcalToken(Long id, String token) {
        Doctor doctor = findById(id);
        doctor.setGcalToken(token);
        doctorRepository.save(doctor);
    }
    
    // Doctores pendientes de verificación
    public List<Doctor> findPendingVerification() {
        return doctorRepository.findByVerifiedFalse();
    }
	
}
