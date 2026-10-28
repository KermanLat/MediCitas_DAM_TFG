package com.mikeldi.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CrearPeticionDTO {
	private Long pacienteId;
    private Long doctorId;
    private LocalDateTime fechaPreferida;
    private List<LocalDate> fechasAlternas;
    private String razon;
    
    
	public CrearPeticionDTO() {
		super();
	}
	public CrearPeticionDTO(Long pacienteId, Long doctorId, LocalDateTime fechaPreferida,
			List<LocalDate> fechasAlternas, String razon) {
		super();
		this.pacienteId = pacienteId;
		this.doctorId = doctorId;
		this.fechaPreferida = fechaPreferida;
		this.fechasAlternas = fechasAlternas;
		this.razon = razon;
	}
	public Long getPacienteId() {
		return pacienteId;
	}
	public void setPacienteId(Long pacienteId) {
		this.pacienteId = pacienteId;
	}
	public Long getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}
	public LocalDateTime getFechaPreferida() {
		return fechaPreferida;
	}
	public void setFechaPreferida(LocalDateTime fechaPreferida) {
		this.fechaPreferida = fechaPreferida;
	}
	public List<LocalDate> getFechasAlternas() {
		return fechasAlternas;
	}
	public void setFechasAlternas(List<LocalDate> fechasAlternas) {
		this.fechasAlternas = fechasAlternas;
	}
	public String getRazon() {
		return razon;
	}
	public void setRazon(String razon) {
		this.razon = razon;
	}
    
    
}
