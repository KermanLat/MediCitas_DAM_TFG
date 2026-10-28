package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mikeldi.demo.model.Cita;
import com.mikeldi.demo.model.Cita.CitaStatus;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
	// Citas de un doctor en un día concreto (dashboard del doctor)
    List<Cita> findByDoctorIdAndScheduledAtBetweenOrderByScheduledAtAsc(
        Long doctorId, LocalDateTime start, LocalDateTime end
    );
    
 // Citas de un paciente en un día concreto (dashboard del paciente)
    List<Cita> findByPacienteIdAndScheduledAtBetweenOrderByScheduledAtAsc(
        Long pacienteId, LocalDateTime start, LocalDateTime end
    );
 
    // Citas de un paciente (próximas citas)
    List<Cita> findByPacienteIdOrderByScheduledAtAsc(Long patientId);
 
    // Citas de un paciente a partir de ahora
    List<Cita> findByPacienteIdAndScheduledAtAfterOrderByScheduledAtAsc(
        Long patientId, LocalDateTime now
    );
 
    // Citas de un doctor con un estado concreto
    List<Cita> findByDoctorIdAndEstado(Long doctorId, CitaStatus status);
 
    // Citas próximas para enviar recordatorios (scheduler)
    List<Cita> findByEstadoAndScheduledAtBetween(
        CitaStatus status, LocalDateTime from, LocalDateTime to
    );
}
