package com.mikeldi.demo.service;

import com.mikeldi.demo.model.SesionNota;
import com.mikeldi.demo.repository.SesionNotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SesionNotaService {
	
	private final SesionNotaRepository noteRepository;

	public SesionNotaService(SesionNotaRepository noteRepository) {
		super();
		this.noteRepository = noteRepository;
	}
	
	// Crear una nota de sesión tras una cita
    @Transactional
    public SesionNota create(SesionNota note) {
        return noteRepository.save(note);
    }
 
    // Obtener todas las notas del expediente de un paciente
    public List<SesionNota> findByMedicalRecord(Long recordId) {
        return noteRepository.findByHistorialMedico_IdOrderByFechaCreadaDesc(recordId);
    }
 
    // Obtener todas las notas escritas por un doctor
    public List<SesionNota> findByDoctor(Long doctorId) {
        return noteRepository.findByDoctor_IdOrderByFechaCreadaDesc(doctorId);
    }
 
    // Obtener todas las notas de una cita concreta
    public List<SesionNota> findByAppointment(Long appointmentId) {
        return noteRepository.findByCita_IdOrderByFechaCreadaDesc(appointmentId);
    }
    
    // Obtener todas las notas de un paciente (para el expediente)
    public List<SesionNota> findByPaciente(Long pacienteId) {
        return noteRepository.findByHistorialMedico_Paciente_IdOrderByFechaCreadaDesc(pacienteId);
    }
 
    // Editar una nota existente
    @Transactional
    public SesionNota update(Long noteId, String newContent) {
    	SesionNota note = findById(noteId);
        note.setContenido(newContent);
        return noteRepository.save(note);
    }
 
    // Borrar una nota
    @Transactional
    public void delete(Long noteId) {
        noteRepository.deleteById(noteId);
    }
 
    public SesionNota findById(Long id) {
        return noteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Nota no encontrada"));
    }

}
