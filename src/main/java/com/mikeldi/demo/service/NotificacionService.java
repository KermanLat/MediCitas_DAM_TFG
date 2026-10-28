package com.mikeldi.demo.service;

import com.mikeldi.demo.model.*;
import com.mikeldi.demo.model.Notificacion.NotifType;
import com.mikeldi.demo.repository.NotificacionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class NotificacionService {
	 private final NotificacionRepository notificationRepository;

	 public NotificacionService(NotificacionRepository notificationRepository) {
		super();
		this.notificationRepository = notificationRepository;
	 }
	 
	// Notificaciones de un usuario (ordenadas por fecha)
	    public List<Notificacion> findByUser(Long userId) {
	        return notificationRepository.findByUsuario_IdOrderByFechaCreadoDesc(userId);
	    }
	 
	    // Notificaciones no vistas (el badge)
	    public List<Notificacion> findUnseenByUser(Long userId) {
	        return notificationRepository.findByUsuario_IdAndVistoFalseOrderByFechaCreadoDesc(userId);
	    }
	 
	    // Contar no vistas (número del badge)
	    public long countUnseen(Long userId) {
	        return notificationRepository.countByUsuario_IdAndVistoFalse(userId);
	    }
	    
	    // Contar citas aceptadas no vistas (badge de "Próximas citas" del paciente)
	    public long countNewAppointments(Long userId) {
	        return notificationRepository.countByUsuario_IdAndTipoAndVistoFalse(
	            userId, NotifType.ACEPTADO
	        );
	    }
	 
	    // Marcar una notificación como vista
	    @Transactional
	    public Notificacion markAsSeen(Long notificationId) {
	        Notificacion notification =  notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notificacion no encontrada con id: " + notificationId));;
	        notification.setVisto(true);
	        return notificationRepository.save(notification);
	    }
	 
	    // Marcar todas las notificaciones de un usuario como vistas
	    @Transactional
	    public void markAllAsSeen(Long userId) {
	        List<Notificacion> unseen = findUnseenByUser(userId);
	        unseen.forEach(n -> {
	            n.setVisto(true);
	            notificationRepository.save(n);
	        });
	    }
	 
	    // Crear notificación: solicitud pendiente → doctor
	    @Transactional
	    public void createPendingRequestNotification(Doctor doctor, PeticionCita request) {
	        Notificacion notification = new Notificacion();
	        notification.setUsuario(doctor);
	        notification.setCita(request.getCita());
	        notification.setTipo(NotifType.SOLICITUD_PENDIENTE);
	        notification.setVisto(false);
	        notificationRepository.save(notification);
	    }
	 
	    // Crear notificación: solicitud aceptada → paciente
	    @Transactional
	    public void createAcceptedNotification(Paciente patient, Cita appointment) {
	        create(patient, appointment, NotifType.ACEPTADO);
	    }
	 
	    // Crear notificación: solicitud rechazada → paciente
	    @Transactional
	    public void createRejectedNotification(Paciente patient, PeticionCita request) {
	        Notificacion notification = new Notificacion();
	        notification.setUsuario(patient);
	        notification.setCita(request.getCita());
	        notification.setTipo(NotifType.RECHAZADA);
	        notification.setVisto(false);
	        notificationRepository.save(notification);
	    }
	 
	    // Crear notificación: recordatorio de cita → paciente (llamado por el scheduler)
	    @Transactional
	    public void createReminderNotification(Paciente patient, Cita appointment) {
	        create(patient, appointment, NotifType.RECORDATORIO);
	    }
	 
	    // Método privado auxiliar
	    private void create(Usuario user, Cita appointment, NotifType type) {
	        Notificacion notification = new Notificacion();
	        notification.setUsuario(user);
	        notification.setCita(appointment);
	        notification.setTipo(type);
	        notification.setVisto(false);
	        notificationRepository.save(notification);
	    }
	 
	    public Notificacion findById(Long id) {
	        return notificationRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada"));
	    }
}
