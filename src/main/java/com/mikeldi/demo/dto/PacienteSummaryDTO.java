package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.Paciente;
import java.time.LocalDate;
import java.time.Period;


public class PacienteSummaryDTO {
	
	private Long   id;
    private String nombre;
    private int    edad;
    private String photoUrl;
    private String enfermedadPrincipal; // viene del HistorialMedico
    
	public PacienteSummaryDTO() {
		super();
	}

	public PacienteSummaryDTO(Long id, String nombre, int edad, String photoUrl, String enfermedadPrincipal) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.edad = edad;
		this.photoUrl = photoUrl;
		this.enfermedadPrincipal = enfermedadPrincipal;
	}
	
	
	public static PacienteSummaryDTO from(Paciente paciente, String enfermedadPrincipal) {
	    int edad = paciente.getFechaNacimiento() != null
	            ? Period.between(paciente.getFechaNacimiento(), LocalDate.now()).getYears()
	            : 0;

	    PacienteSummaryDTO dto = new PacienteSummaryDTO();
	    dto.setId(paciente.getId());
	    dto.setNombre(paciente.getNombre());
	    dto.setEdad(edad);
	    dto.setPhotoUrl(paciente.getPhotoUrl());
	    dto.setEnfermedadPrincipal(enfermedadPrincipal);
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

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getEnfermedadPrincipal() {
		return enfermedadPrincipal;
	}

	public void setEnfermedadPrincipal(String enfermedadPrincipal) {
		this.enfermedadPrincipal = enfermedadPrincipal;
	}
	
	
	
	
	
    
    

}
