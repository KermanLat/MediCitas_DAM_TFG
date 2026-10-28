package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.Doctor;

public class DoctorProfileDTO {
	
	private Long   id;
    private String nombre;
    private String email;
    private String photoUrl;
    private String especialidad;
    private String licenciaMedica;
    private Boolean verificado;
    
    
	public DoctorProfileDTO() {
		super();
	}


	public DoctorProfileDTO(Long id, String nombre, String email, String photoUrl, String especialidad,
			String licenciaMedica, Boolean verificado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.photoUrl = photoUrl;
		this.especialidad = especialidad;
		this.licenciaMedica = licenciaMedica;
		this.verificado = verificado;
	}
    
	// Convierte una entidad Doctor a DTO (sin exponer password ni gcalToken)
	public static DoctorProfileDTO from(Doctor doctor) {
	    DoctorProfileDTO dto = new DoctorProfileDTO();
	    dto.setId(doctor.getId());
	    dto.setNombre(doctor.getNombre());
	    dto.setEmail(doctor.getEmail());
	    dto.setPhotoUrl(doctor.getPhotoUrl());
	    dto.setEspecialidad(doctor.getEspecialidad());
	    dto.setLicenciaMedica(doctor.getLicenciaMedica());
	    dto.setVerificado(doctor.getVerificado());
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


	public String getEspecialidad() {
		return especialidad;
	}


	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}


	public String getLicenciaMedica() {
		return licenciaMedica;
	}


	public void setLicenciaMedica(String licenciaMedica) {
		this.licenciaMedica = licenciaMedica;
	}


	public Boolean getVerificado() {
		return verificado;
	}


	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}
    
    
    

}
