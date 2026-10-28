package com.mikeldi.demo.dto;

public class LoginResponseDTO {
	
	private String token;
    private String role;
    private Long id;
    private String nombre;
    private String email;
 
    public LoginResponseDTO() {
    }
 
    public LoginResponseDTO(String token, String role, Long id, String nombre, String email) {
        this.token = token;
        this.role = role;
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }
 
    public String getToken() {
        return token;
    }
 
    public void setToken(String token) {
        this.token = token;
    }
 
    public String getRole() {
        return role;
    }
 
    public void setRole(String role) {
        this.role = role;
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
}
