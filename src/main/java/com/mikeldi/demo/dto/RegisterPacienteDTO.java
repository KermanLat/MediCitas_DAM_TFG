package com.mikeldi.demo.dto;

public class RegisterPacienteDTO {
	
	private String email;
    private String password;
    private String nombre;
    private String photoUrl;
    private String fechaNacimiento;   // "1990-05-20"
    private String telefono;
    private String numSeguro;
 
    public RegisterPacienteDTO() {
    }
 
    public RegisterPacienteDTO(String email, String password, String nombre, String photoUrl,
                                String fechaNacimiento, String telefono, String numSeguro) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.photoUrl = photoUrl;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.numSeguro = numSeguro;
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
 
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
 
    public void setFechaNacimiento(String fechaNacimiento) {
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
