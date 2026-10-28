package com.mikeldi.demo.dto;

public class RegisterDoctorDTO {
	private String email;
    private String password;
    private String nombre;
    private String photoUrl;
    private String especialidad;
    private String licenciaMedica;
    private String docUrl;
 
    public RegisterDoctorDTO() {
    }
 
    public RegisterDoctorDTO(String email, String password, String nombre, String photoUrl,
                              String especialidad, String licenciaMedica, String docUrl) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.photoUrl = photoUrl;
        this.especialidad = especialidad;
        this.licenciaMedica = licenciaMedica;
        this.docUrl = docUrl;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
 
    public String getDocUrl() {
        return docUrl;
    }
 
    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }
}
