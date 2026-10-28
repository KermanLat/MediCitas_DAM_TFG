package com.mikeldi.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "citas")
public class Cita {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	// Solicitud que originó esta cita (1:1)
    @OneToOne(optional = false)
    @JoinColumn(name = "peticion_id", nullable = false)
    private PeticionCita peticion;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
    
    @Column(name = "fecha_programada", nullable = false)
    private LocalDateTime scheduledAt;
 
    private String sala;
    
    @Column(name = "gcal_event_id")
    private String gcalEventId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CitaStatus estado = CitaStatus.PROGRAMADO;
    
 // Relaciones
    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL)
    private List<SesionNota> sesionNotas;
 
    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL)
    private List<Notificacion> notificacioness;
    
    public enum CitaStatus {
        PROGRAMADO, COMPLETADO, CANCELADO
    }

	public Cita() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PeticionCita getPeticion() {
		return peticion;
	}

	public void setPeticion(PeticionCita peticion) {
		this.peticion = peticion;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public LocalDateTime getFechaProgramada() {
		return scheduledAt;
	}

	public void setFechaProgramada(LocalDateTime fechaProgramada) {
		this.scheduledAt = fechaProgramada;
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getGcalEventId() {
		return gcalEventId;
	}

	public void setGcalEventId(String gcalEventId) {
		this.gcalEventId = gcalEventId;
	}

	public CitaStatus getEstado() {
		return estado;
	}

	public void setEstado(CitaStatus estado) {
		this.estado = estado;
	}

	public List<SesionNota> getSesionNotas() {
		return sesionNotas;
	}

	public void setSesionNotas(List<SesionNota> sesionNotas) {
		this.sesionNotas = sesionNotas;
	}

	public List<Notificacion> getNotificacioness() {
		return notificacioness;
	}

	public void setNotificacioness(List<Notificacion> notificacioness) {
		this.notificacioness = notificacioness;
	}
    
    
    
    
}
