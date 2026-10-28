package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mikeldi.demo.model.Mensaje;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
	// Mensajes enviados por un doctor
    List<Mensaje> findBySenderIdOrderByFechaEnvioDesc(Long senderId);
 
    // Mensajes recibidos por un doctor
    List<Mensaje> findByReceiverIdOrderByFechaEnvioDesc(Long receiverId);
 
    // Conversación entre dos doctores (en ambas direcciones) es Mensaje y fechaEnvio, pq esta query lo lee JPA, que luego se lo traduce a mysql
    @Query("SELECT m FROM Mensaje m WHERE " +
    	       "(m.sender.id = :doctorA AND m.receiver.id = :doctorB) OR " +
    	       "(m.sender.id = :doctorB AND m.receiver.id = :doctorA) " +
    	       "ORDER BY m.fechaEnvio ASC")
    List<Mensaje> findConversation(@Param("doctorA") Long doctorA,
                                   @Param("doctorB") Long doctorB);
    //Envia el ultimo mensaje de la conversacion
    @Query("SELECT m FROM Mensaje m WHERE m.id IN (" +
    	       "SELECT MAX(m2.id) FROM Mensaje m2 WHERE " +
    	       "m2.sender.id = :doctorId OR m2.receiver.id = :doctorId " +
    	       "GROUP BY CASE WHEN m2.sender.id = :doctorId THEN m2.receiver.id ELSE m2.sender.id END" +
    	       ") ORDER BY m.fechaEnvio DESC")
    	List<Mensaje> findLastMessagePerConversation(@Param("doctorId") Long doctorId);
 
    // Mensajes no leídos de un doctor
    List<Mensaje> findByReceiverIdAndLeidoFalse(Long receiverId);
 
    // Contar mensajes no leídos
    long countByReceiverIdAndLeidoFalse(Long receiverId);
}
