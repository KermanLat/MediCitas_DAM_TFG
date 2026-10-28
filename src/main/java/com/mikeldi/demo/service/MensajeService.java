package com.mikeldi.demo.service;

import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.model.Mensaje;
import com.mikeldi.demo.repository.MensajeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MensajeService {
	private final MensajeRepository messageRepository;
    private final DoctorService doctorService;
	public MensajeService(MensajeRepository messageRepository, DoctorService doctorService) {
		super();
		this.messageRepository = messageRepository;
		this.doctorService = doctorService;
	}
    
	// Enviar un mensaje de un doctor a otro
    @Transactional
    public Mensaje send(Long senderId, Long receiverId, String content) {
        Doctor sender   = doctorService.findById(senderId);
        Doctor receiver = doctorService.findById(receiverId);
 
        Mensaje message = new Mensaje();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContenido(content);
        message.setLeido(false);
 
        return messageRepository.save(message);
    }
 
    // Obtener conversación entre dos doctores
    public List<Mensaje> findConversation(Long doctorA, Long doctorB) {
        return messageRepository.findConversation(doctorA, doctorB);
    }
    
    // Obtener el ultimo mensaje de una conversacion entre dos doctores
    public List<Mensaje> findConversations(Long doctorId) {
        return messageRepository.findLastMessagePerConversation(doctorId);
    }
 
    // Mensajes recibidos por un doctor (buzón)
    public List<Mensaje> findReceived(Long doctorId) {
        return messageRepository.findByReceiverIdOrderByFechaEnvioDesc(doctorId);
    }
 
    // Mensajes no leídos
    public List<Mensaje> findUnread(Long doctorId) {
        return messageRepository.findByReceiverIdAndLeidoFalse(doctorId);
    }
 
    // Contar mensajes no leídos (badge del buzón)
    public long countUnread(Long doctorId) {
        return messageRepository.countByReceiverIdAndLeidoFalse(doctorId);
    }
 
    // Marcar mensaje como leído
    @Transactional
    public Mensaje markAsRead(Long messageId) {
        Mensaje message = findById(messageId);
        message.setLeido(true);
        return messageRepository.save(message);
    }
 
    // Marcar todos los mensajes de una conversación como leídos
    @Transactional
    public void markConversationAsRead(Long receiverId, Long senderId) {
        List<Mensaje> messages = messageRepository.findConversation(senderId, receiverId);
        messages.stream()
            .filter(m -> m.getReceiver().getId().equals(receiverId) && !m.getLeido())
            .forEach(m -> {
                m.setLeido(true);
                messageRepository.save(m);
            });
    }
 
    public Mensaje findById(Long id) {
        return messageRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Mensaje no encontrado"));
    }
}
