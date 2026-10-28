package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mikeldi.demo.model.Doctor;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	
	// Buscar doctor por email (heredado de User)
    Optional<Doctor> findByEmail(String email);
 
    // Buscar doctores por especialidad
    List<Doctor> findBySpecialty(String especialidad);
 
    // Solo doctores verificados
    List<Doctor> findByVerifiedTrue();
 
    // Doctores pendientes de verificar
    List<Doctor> findByVerifiedFalse();
 
    // Buscar por nombre (heredado de User)
    List<Doctor> findByNameContainingIgnoreCase(String nombre);

}
