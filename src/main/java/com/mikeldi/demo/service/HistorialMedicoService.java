package com.mikeldi.demo.service;

import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.model.HistorialMedico;
import com.mikeldi.demo.model.Paciente;
import com.mikeldi.demo.repository.DoctorRepository;
import com.mikeldi.demo.repository.HistorialMedicoRepository;
import com.mikeldi.demo.service.PacienteService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistorialMedicoService {
	
	private final HistorialMedicoRepository recordRepository;
    private final DoctorRepository doctorRepository;
    private final PacienteService patientService;
    
	public HistorialMedicoService(HistorialMedicoRepository recordRepository, DoctorRepository doctorRepository, PacienteService patientService) {
		super();
		this.recordRepository = recordRepository;
		this.doctorRepository = doctorRepository;
		this.patientService = patientService;
	}
    
	// Obtener expediente de un paciente
    public HistorialMedico findByPatient(Long pacienteId) {
    	return recordRepository.findByPaciente_Id(pacienteId)
    	        .orElseThrow(() -> new IllegalArgumentException("Expediente no encontrado"));
    }
 
    // Crear expediente al registrar un paciente
    @Transactional
    public HistorialMedico create(Paciente patient) {
        if (recordRepository.existsByPaciente_Id(patient.getId())) {
            throw new IllegalStateException("El paciente ya tiene un expediente");
        }
        HistorialMedico record = new HistorialMedico();
        record.setPaciente(patient);
        return recordRepository.save(record);
    }
 
    // Actualizar expediente (el doctor edita info médica)
    @Transactional
    public HistorialMedico update(Long patientId, HistorialMedico updated) {
    	HistorialMedico record;
    	
    	 if (recordRepository.existsByPaciente_Id(patientId)) {
    	        record = findByPatient(patientId);
    	    } else {
    	        Paciente paciente = patientService.findById(patientId);
    	        record = new HistorialMedico();
    	        record.setPaciente(paciente);
    	    }
    	 
        record.setEnfermedadPrincipal(updated.getEnfermedadPrincipal());
        record.setAlergias(updated.getAlergias());
        record.setMedicamentosActuales(updated.getMedicamentosActuales());
        record.setTipoSangre(updated.getTipoSangre());
        return recordRepository.save(record);
    }
 
    // Añadir doctor al expediente (relación gestiona N:M)
    @Transactional
    public HistorialMedico addDoctor(Long patientId, Long doctorId) {
        HistorialMedico record = findByPatient(patientId);
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado"));
 
        if (!record.getDoctores().contains(doctor)) {
            record.getDoctores().add(doctor);
            doctor.getManagedRecords().add(record);
            doctorRepository.save(doctor);
        }
        return recordRepository.save(record);
    }
 
    // Eliminar doctor del expediente
    @Transactional
    public HistorialMedico removeDoctor(Long patientId, Long doctorId) {
        HistorialMedico record = findByPatient(patientId);
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado"));
 
        record.getDoctores().remove(doctor);
        doctor.getManagedRecords().remove(record);
        doctorRepository.save(doctor);
        return recordRepository.save(record);
    }
    
}
