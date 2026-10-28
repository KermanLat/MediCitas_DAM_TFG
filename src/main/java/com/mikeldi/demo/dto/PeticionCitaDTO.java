package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.PeticionCita;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PeticionCitaDTO {
	private Long               id;
    private LocalDateTime      fechaPreferida;
    private List<LocalDate>    fechaAlterna;
    private String             razon;
    private String             estado;
    private LocalDateTime      fechaCreada;
 
    private DoctorSummaryDTO   doctor;
    private PacienteSummaryDTO  paciente;
    
    
	public PeticionCitaDTO() {
		super();
	}


	public PeticionCitaDTO(Long id, LocalDateTime fechaPreferida, List<LocalDate> fechaAlterna, String razon,
			String estado, LocalDateTime fechaCreada, DoctorSummaryDTO doctor, PacienteSummaryDTO patient) {
		super();
		this.id = id;
		this.fechaPreferida = fechaPreferida;
		this.fechaAlterna = fechaAlterna;
		this.razon = razon;
		this.estado = estado;
		this.fechaCreada = fechaCreada;
		this.doctor = doctor;
		this.paciente = patient;
	}
	
	
	public static PeticionCitaDTO from(PeticionCita req, String enfermedadPrincipal) {
		PeticionCitaDTO dto = new PeticionCitaDTO();
		dto.setId(req.getId());
		dto.setFechaPreferida(req.getFechaPreferida());
		dto.setFechaAlterna(req.getFechaAlterna());
		dto.setRazon(req.getRazon());
		dto.setEstado(req.getEstado().name()); //esto no es un reqStatus
		dto.setFechaCreada(req.getFechaCreada());
		
		dto.setDoctor(DoctorSummaryDTO.from(req.getDoctor()));
		dto.setPaciente(PacienteSummaryDTO.from(req.getPaciente(), enfermedadPrincipal));
		return dto;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public LocalDateTime getFechaPreferida() {
		return fechaPreferida;
	}


	public void setFechaPreferida(LocalDateTime fechaPreferida) {
		this.fechaPreferida = fechaPreferida;
	}


	public List<LocalDate> getFechaAlterna() {
		return fechaAlterna;
	}


	public void setFechaAlterna(List<LocalDate> fechaAlterna) {
		this.fechaAlterna = fechaAlterna;
	}


	public String getRazon() {
		return razon;
	}


	public void setRazon(String razon) {
		this.razon = razon;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
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


	public PacienteSummaryDTO getPaciente() {
		return paciente;
	}


	public void setPaciente(PacienteSummaryDTO patient) {
		this.paciente = patient;
	}
    
	
	
    

}
