package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.Paciente;
import java.time.LocalDate;

public class PacienteProfileDTO {
	private Long      id;
    private String    nombre;
    private String    email;
    private String    photoUrl;
    private LocalDate fechaNacimiento;
    private String    telefono;
    private String    numSeguro;
    
	public PacienteProfileDTO() {
		super();
	}

	public PacienteProfileDTO(Long id, String nombre, String email, String photoUrl, LocalDate fechaNacimiento,
			String telefono, String numSeguro) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.photoUrl = photoUrl;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.numSeguro = numSeguro;
	}
    
	public static PacienteProfileDTO from(Paciente paciente) {
		PacienteProfileDTO dto = new PacienteProfileDTO();
		dto.setId(paciente.getId());
		dto.setNombre(paciente.getNombre());
		dto.setEmail(paciente.getEmail());
		dto.setPhotoUrl(paciente.getPhotoUrl());
		dto.setFechaNacimiento(paciente.getFechaNacimiento());
		dto.setTelefono(paciente.getTelefono());
		dto.setNumSeguro(paciente.getNumSeguro());
		return dto;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getNumSeguro() {
		return numSeguro;
	}

	public void setNumSeguro(String numSeguro) {
		this.numSeguro = numSeguro;
	}
	
	
    
}
