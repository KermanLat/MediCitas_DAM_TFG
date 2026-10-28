package com.mikeldi.demo.service;

import com.mikeldi.demo.model.*;
import com.mikeldi.demo.model.PeticionCita.ReqStatus;
import com.mikeldi.demo.repository.PeticionCitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PeticionCitaService {
	
	private final PeticionCitaRepository requestRepository;
    private final CitaService appointmentService;
    private final NotificacionService notificationService;
    
	public PeticionCitaService(PeticionCitaRepository requestRepository, CitaService appointmentService,
			NotificacionService notificationService) {
		super();
		this.requestRepository = requestRepository;
		this.appointmentService = appointmentService;
		this.notificationService = notificationService;
	}
    
	// Crear una nueva solicitud de cita (acción del paciente)
    @Transactional
    public PeticionCita create(PeticionCita request) {
        request.setEstado(ReqStatus.PENDIENTE);
        PeticionCita saved = requestRepository.save(request);
 
        // Notificar al doctor que tiene una solicitud pendiente
        notificationService.createPendingRequestNotification(
            request.getDoctor(), saved
        );
        return saved;
    }
 
    // Aceptar solicitud (acción del doctor)
    @Transactional
    public Cita accept(Long requestId, LocalDateTime fecha, String sala) {
        PeticionCita request = findById(requestId);
        request.setEstado(ReqStatus.ACEPTADO);
        requestRepository.save(request);
 
        // Crear la cita confirmada con la fecha y sala elegidas por el doctor
        Cita appointment = appointmentService.createFromRequest(request, fecha, sala);
 
        // Notificar al paciente que fue aceptada
        notificationService.createAcceptedNotification(
            request.getPaciente(), appointment
        );
        return appointment;
    }
 
    // Rechazar solicitud (acción del doctor)
    @Transactional
    public PeticionCita reject(Long requestId) {
        PeticionCita request = findById(requestId);
        request.setEstado(ReqStatus.RECHAZADO);
        requestRepository.save(request);
 
        // Notificar al paciente que fue rechazada
        notificationService.createRejectedNotification(
            request.getPaciente(), request
        );
        return request;
    }
 
    // Solicitudes pendientes de un doctor (dashboard del doctor)
    public List<PeticionCita> findPendingByDoctor(Long doctorId) {
        return requestRepository.findByDoctor_IdAndEstado(doctorId, ReqStatus.PENDIENTE);
    }
 
    // Solicitudes de un paciente (lo que el paciente está esperando)
    public List<PeticionCita> findByPatient(Long patientId) {
        return requestRepository.findByPaciente_Id(patientId);
    }
 
    // Solicitudes pendientes de un paciente
    public List<PeticionCita> findPendingByPatient(Long patientId) {
        return requestRepository.findByPaciente_IdAndEstado(patientId, ReqStatus.PENDIENTE);
    }
 
    public PeticionCita findById(Long id) {
        return requestRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
    }
    
    

}
