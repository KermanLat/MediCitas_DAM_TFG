package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.Doctor;

public class DoctorSummaryDTO {
	
	private Long   id;
    private String nombre;
    private String especialidad;
    private String photoUrl;
    
	public DoctorSummaryDTO() {
		super();
	}
	public DoctorSummaryDTO(Long id, String nombre, String especialidad, String photoUrl) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.especialidad = especialidad;
		this.photoUrl = photoUrl;
	}
    
	// Convierte una entidad Doctor a DTO (sin exponer password ni gcalToken)
		public static DoctorSummaryDTO from(Doctor doctor) {
		    DoctorSummaryDTO dto = new DoctorSummaryDTO();
		    dto.setId(doctor.getId());
		    dto.setNombre(doctor.getNombre());		    
		    dto.setPhotoUrl(doctor.getPhotoUrl());
		    dto.setEspecialidad(doctor.getEspecialidad());
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
		public String getEspecialidad() {
			return especialidad;
		}
		public void setEspecialidad(String especialidad) {
			this.especialidad = especialidad;
		}
		public String getPhotoUrl() {
			return photoUrl;
		}
		public void setPhotoUrl(String photoUrl) {
			this.photoUrl = photoUrl;
		}
		
		
    
}
