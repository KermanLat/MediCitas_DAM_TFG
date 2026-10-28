package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.SesionNota;
import java.time.LocalDateTime;

public class SesionNotaDTO {
	
	 private Long             id;
	 private String           contenido;
	 private LocalDateTime    fechaCreada;
	 
	// Info resumida del doctor que escribió la nota
	private DoctorSummaryDTO doctor;
	 
	// Id de la cita en la que se escribió
	private Long             citaId;

	public SesionNotaDTO() {
		super();
	}

	public SesionNotaDTO(Long id, String contenido, LocalDateTime fechaCreada, DoctorSummaryDTO doctor, Long citaId) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.fechaCreada = fechaCreada;
		this.doctor = doctor;
		this.citaId = citaId;
	}
	
	public static SesionNotaDTO from(SesionNota nota) {
		SesionNotaDTO dto = new SesionNotaDTO();
		dto.setId(nota.getId());
		dto.setContenido(nota.getContenido());
		dto.setFechaCreada(nota.getFechaCreada());
		
		dto.setDoctor(DoctorSummaryDTO.from(nota.getDoctor()));
		dto.setCitaId(nota.getCita().getId()); // me da duda esta
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

	public LocalDateTime getFechaCreada() {
		return fechaCreada;
	}

	public void setFechaCreada(LocalDateTime fechaCreada) {
		this.fechaCreada = fechaCreada;
	}

	public DoctorSummaryDTO getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorSummaryDTO doctor) {
		this.doctor = doctor;
	}

	public Long getCitaId() {
		return citaId;
	}

	public void setCitaId(Long citaId) {
		this.citaId = citaId;
	}
	
	
	
}
