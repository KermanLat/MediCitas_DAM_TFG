package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.Mensaje;
import java.time.LocalDateTime;

public class MensajeDTO {
	
	private Long             id;
    private String           contenido;
    private LocalDateTime    fechaEnvio;
    private Boolean          leido;
 
    // Info resumida del emisor y receptor
    private DoctorSummaryDTO sender;
    private DoctorSummaryDTO receiver;
    
	public MensajeDTO() {
		super();
	}

	public MensajeDTO(Long id, String contenido, LocalDateTime fechaEnvio, Boolean leido, DoctorSummaryDTO sender,
			DoctorSummaryDTO receiver) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.fechaEnvio = fechaEnvio;
		this.leido = leido;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public static MensajeDTO from(Mensaje mensaje) {
		MensajeDTO dto = new MensajeDTO();
		
		dto.setId(mensaje.getId());
		dto.setContenido(mensaje.getContenido());
		dto.setFechaEnvio(mensaje.getFechaEnvio());
		dto.setLeido(mensaje.getLeido());
		
		dto.setSender(DoctorSummaryDTO.from(mensaje.getSender()));
		dto.setReceiver(DoctorSummaryDTO.from(mensaje.getReceiver()));
		
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Boolean getLeido() {
		return leido;
	}

	public void setLeido(Boolean leido) {
		this.leido = leido;
	}

	public DoctorSummaryDTO getSender() {
		return sender;
	}

	public void setSender(DoctorSummaryDTO sender) {
		this.sender = sender;
	}

	public DoctorSummaryDTO getReceiver() {
		return receiver;
	}

	public void setReceiver(DoctorSummaryDTO receiver) {
		this.receiver = receiver;
	}
    
    

}
