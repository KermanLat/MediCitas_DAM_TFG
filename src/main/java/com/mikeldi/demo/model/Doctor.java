package com.mikeldi.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.*;

@Entity
@Table(name = "doctores")
@PrimaryKeyJoinColumn(name = "user_id")
public class Doctor extends Usuario {
	
	private String specialty;
	
	@Column(name = "licencia_med")
	private String licenciaMedica;
	
	@Column(name = "doc_url")
	private String docUrl;
	
	@Column(nullable = false)
	private Boolean verified = false;
	
	@Column(name = "gcal_token", length = 1000)
    private String gcalToken;
	
	// Relaciones
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<PeticionCita> peticionCitas;
 
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<SesionNota> sessionNotes;
 
    @ManyToMany
    @JoinTable(
        name = "doctor_medical_record",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "record_id")
    )
    private List<HistorialMedico> managedRecords;
 
    // Mensajes enviados
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Mensaje> enviarMensajes;
 
    // Mensajes recibidos
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Mensaje> mensajeRecividos;

	public Doctor() {
		super();
	}
	
	

	public Doctor(String especialidad, String licenciaMedica, String docUrl, Boolean verificado, String gcalToken,
			List<PeticionCita> appointmentRequests, List<SesionNota> sessionNotes, List<HistorialMedico> managedRecords,
			List<Mensaje> sentMessages, List<Mensaje> receivedMessages) {
		super();
		this.specialty = especialidad;
		this.licenciaMedica = licenciaMedica;
		this.docUrl = docUrl;
		this.verified = verificado;
		this.gcalToken = gcalToken;
		this.peticionCitas = appointmentRequests;
		this.sessionNotes = sessionNotes;
		this.managedRecords = managedRecords;
		this.enviarMensajes = sentMessages;
		this.mensajeRecividos = receivedMessages;
	}

	public String getEspecialidad() {
		return specialty;
	}

	public void setEspecialidad(String especialidad) {
		this.specialty = especialidad;
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

	public Boolean getVerificado() {
		return verified;
	}

	public void setVerificado(Boolean verificado) {
		this.verified = verificado;
	}

	public String getGcalToken() {
		return gcalToken;
	}

	public void setGcalToken(String gcalToken) {
		this.gcalToken = gcalToken;
	}

	public List<PeticionCita> getAppointmentRequests() {
		return peticionCitas;
	}

	public void setAppointmentRequests(List<PeticionCita> appointmentRequests) {
		this.peticionCitas = appointmentRequests;
	}

	public List<SesionNota> getSessionNotes() {
		return sessionNotes;
	}

	public void setSessionNotes(List<SesionNota> sessionNotes) {
		this.sessionNotes = sessionNotes;
	}

	public List<HistorialMedico> getManagedRecords() {
		return managedRecords;
	}

	public void setManagedRecords(List<HistorialMedico> managedRecords) {
		this.managedRecords = managedRecords;
	}

	public List<Mensaje> getSentMessages() {
		return enviarMensajes;
	}

	public void setSentMessages(List<Mensaje> sentMessages) {
		this.enviarMensajes = sentMessages;
	}

	public List<Mensaje> getReceivedMessages() {
		return mensajeRecividos;
	}

	public void setReceivedMessages(List<Mensaje> receivedMessages) {
		this.mensajeRecividos = receivedMessages;
	}
    
    

}
