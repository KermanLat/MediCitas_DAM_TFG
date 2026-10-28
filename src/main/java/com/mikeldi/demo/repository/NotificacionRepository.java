package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mikeldi.demo.model.Notificacion;
import com.mikeldi.demo.model.Notificacion.NotifType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
	// Todas las notificaciones de un usuario ordenadas por fecha
    List<Notificacion> findByUsuario_IdOrderByFechaCreadoDesc(Long userId);
 
    // Notificaciones no vistas de un usuario (el badge)
    List<Notificacion> findByUsuario_IdAndVistoFalseOrderByFechaCreadoDesc(Long userId);
 
    // Contar notificaciones no vistas (número del badge)
    long countByUsuario_IdAndVistoFalse(Long userId);
 
    // Notificaciones de un tipo concreto para un usuario
    List<Notificacion> findByUsuario_IdAndTipo(Long userId, NotifType type);
 
    // Notificaciones de una cita concreta
    List<Notificacion> findByCitaId(Long appointmentId);
    
 // Contar notificaciones de tipo ACCEPTED no vistas (badge de próximas citas)
    long countByUsuario_IdAndTipoAndVistoFalse(Long userId, NotifType type);
}
