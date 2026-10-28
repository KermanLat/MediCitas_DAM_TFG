package com.mikeldi.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario { //el inheritance es pq un user sera o paciente o medico y con otras cosas en paciente y medico hara que cree tres tablas separadas en la BD compartiendo el mismo id
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	@Column(name = "photo_url")
	private String photoUrl;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Rol rol;
	
	@Column(name = "fecha_creado", nullable = false, updatable = false)
	private LocalDateTime fechaCreado;
	
	@PrePersist
    protected void onCreate() {
        this.fechaCreado = LocalDateTime.now();
    }
	
	public enum Rol {
        DOCTOR, PACIENTE
    }

	public Usuario() {
		super();
	}

	public Usuario(Long id, String email, String password, String nombre, String photoUrl, Rol rol,
			LocalDateTime fechaCreado) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = nombre;
		this.photoUrl = photoUrl;
		this.rol = rol;
		this.fechaCreado = fechaCreado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return name;
	}

	public void setNombre(String nombre) {
		this.name = nombre;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public LocalDateTime getFechaCreado() {
		return fechaCreado;
	}

	public void setFechaCreado(LocalDateTime fechaCreado) {
		this.fechaCreado = fechaCreado;
	}
	
	
}
