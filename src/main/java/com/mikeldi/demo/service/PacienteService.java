package com.mikeldi.demo.service;

import com.mikeldi.demo.model.Paciente;
import com.mikeldi.demo.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;	
	
	public PacienteService(PacienteRepository pacienteRepository) {
		this.pacienteRepository = pacienteRepository;
	}
	
	// Obtener paciente por id
    public Paciente findById(Long id) {
        return pacienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    }
    
    //Obtener paciente por Email
    public Paciente findByEmail(String email) {
    	return pacienteRepository.findByEmail(email)
    			.orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    }
 
    // Obtener todos los pacientes (listado del doctor)
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }
 
    // Buscar paciente por nombre
    public List<Paciente> searchByName(String name) {
        return pacienteRepository.findByNameContainingIgnoreCase(name);
    }
    
 // Actualizar perfil del paciente
    @Transactional
    public Paciente updateProfile(Long id, Paciente updated) {
        Paciente patient = findById(id);
        patient.setNombre(updated.getNombre());
        patient.setTelefono(updated.getTelefono());
        patient.setPhotoUrl(updated.getPhotoUrl());
        patient.setFechaNacimiento(updated.getFechaNacimiento());
        return pacienteRepository.save(patient);
    }
 
    // Dar de alta al paciente (el doctor lo marca como dado de alta)
    @Transactional
    public void discharge(Long id) {
        Paciente patient = findById(id);
        // Aquí podrías añadir lógica adicional como
        // cancelar citas futuras, cerrar expediente, etc.
        pacienteRepository.save(patient);
    }
	
}
