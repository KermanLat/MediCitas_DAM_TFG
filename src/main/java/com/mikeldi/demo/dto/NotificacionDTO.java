package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.Notificacion;
import java.time.LocalDateTime;

public class NotificacionDTO {
	
	private Long          id;
    private String        tipo;       // ACCEPTED, REJECTED, REMINDER, PENDING_REQUEST
    private Boolean       visto;
    private LocalDateTime fechaCreado;
    
 // Info básica de la cita relacionada
    private Long          citaId;
    private LocalDateTime citaFecha;
    private String        citaSala;
    
	public NotificacionDTO() {
		super();
	}

	public NotificacionDTO(Long id, String tipo, Boolean visto, LocalDateTime fechaCreado, Long citaId,
			LocalDateTime citaFecha, String citaSala) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.visto = visto;
		this.fechaCreado = fechaCreado;
		this.citaId = citaId;
		this.citaFecha = citaFecha;
		this.citaSala = citaSala;
	}
	
	
	public static NotificacionDTO from(Notificacion notificacion) {
		NotificacionDTO dto = new NotificacionDTO();
		dto.setId(notificacion.getId());
		dto.setTipo(notificacion.getTipo().name());
		dto.setFechaCreado(notificacion.getFechaCreado());
		
		dto.setCitaId(notificacion.getCita().getId());
		dto.setCitaFecha(notificacion.getCita().getFechaProgramada());
		dto.setCitaSala(notificacion.getCita().getSala());
		
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getVisto() {
		return visto;
	}

	public void setVisto(Boolean visto) {
		this.visto = visto;
	}

	public LocalDateTime getFechaCreado() {
		return fechaCreado;
	}

	public void setFechaCreado(LocalDateTime fechaCreado) {
		this.fechaCreado = fechaCreado;
	}

	public Long getCitaId() {
		return citaId;
	}

	public void setCitaId(Long citaId) {
		this.citaId = citaId;
	}

	public LocalDateTime getCitaFecha() {
		return citaFecha;
	}

	public void setCitaFecha(LocalDateTime citaFecha) {
		this.citaFecha = citaFecha;
	}

	public String getCitaSala() {
		return citaSala;
	}

	public void setCitaSala(String citaSala) {
		this.citaSala = citaSala;
	}
    
	
	
    
    
    

}
