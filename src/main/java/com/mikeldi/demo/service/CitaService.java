package com.mikeldi.demo.service;

import com.mikeldi.demo.model.Cita;
import com.mikeldi.demo.model.Cita.CitaStatus;
import com.mikeldi.demo.model.PeticionCita;
import com.mikeldi.demo.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {
	
	private final CitaRepository appointmentRepository;
    private final GoogleCalendarService googleCalendarService;
    
	public CitaService(CitaRepository appointmentRepository, GoogleCalendarService googleCalendarService) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.googleCalendarService = googleCalendarService;
	}
    
	// Crear cita a partir de una solicitud aceptada
    @Transactional
    public Cita createFromRequest(PeticionCita request, LocalDateTime fecha, String sala) {
        Cita appointment = new Cita();
        appointment.setPeticion(request);
        appointment.setDoctor(request.getDoctor());
        appointment.setPaciente(request.getPaciente());
        appointment.setFechaProgramada(fecha);
        appointment.setSala(sala);
        appointment.setEstado(CitaStatus.PROGRAMADO);
 
        Cita saved = appointmentRepository.save(appointment);
 
        // Crear evento en Google Calendar
        String gcalEventId = googleCalendarService.createEvent(saved);
        saved.setGcalEventId(gcalEventId);
 
        return appointmentRepository.save(saved);
    }
 
    // Citas del doctor hoy (dashboard)
    public List<Cita> findTodayByDoctor(Long doctorId) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return appointmentRepository
            .findByDoctorIdAndScheduledAtBetweenOrderByScheduledAtAsc(
                doctorId, startOfDay, endOfDay
            );
    }
    
 // Citas del paciente hoy (dashboard)
    public List<Cita> findTodayByPatient(Long pacienteId) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return appointmentRepository
            .findByPacienteIdAndScheduledAtBetweenOrderByScheduledAtAsc(
                pacienteId, startOfDay, endOfDay
            );
    }
 
    // Próximas citas de un paciente
    public List<Cita> findUpcomingByPatient(Long patientId) {
        return appointmentRepository
            .findByPacienteIdAndScheduledAtAfterOrderByScheduledAtAsc(
                patientId, LocalDateTime.now()
            );
    }
 
    // Cambiar horario de una cita
    @Transactional
    public Cita reschedule(Long citaId, LocalDateTime nuevaFecha) {
        Cita cita = appointmentRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + citaId));
        cita.setFechaProgramada(nuevaFecha);

        googleCalendarService.updateEvent(cita);

        return appointmentRepository.save(cita);
    }
 
    // Cancelar una cita
    @Transactional
    public Cita cancel(Long citaId) {
        Cita cita = appointmentRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + citaId));
        cita.setEstado(Cita.CitaStatus.CANCELADO);

        googleCalendarService.deleteEvent(cita.getGcalEventId(),
                cita.getDoctor().getGcalToken());

        return appointmentRepository.save(cita);
    }
 
    @Transactional
    public Cita complete(Long citaId) {
        Cita cita = appointmentRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + citaId));
        cita.setEstado(Cita.CitaStatus.COMPLETADO);
        return appointmentRepository.save(cita);
    }
 
    public Cita findById(Long id) {
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
    }
    
    public List<Cita> findByDoctorAndRange(Long doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository
            .findByDoctorIdAndScheduledAtBetweenOrderByScheduledAtAsc(doctorId, start, end);
    }
    

}
