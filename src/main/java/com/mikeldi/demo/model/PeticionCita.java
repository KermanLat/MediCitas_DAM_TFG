package com.mikeldi.demo.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "peticion_citas")
public class PeticionCita {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
	private Paciente paciente;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
	
	@Column(name = "fecha_preferida", nullable = false)
	private LocalDateTime fechaPreferida;
	
	 // Fechas alternativas almacenadas como JSON o texto separado por comas
    @ElementCollection
    @CollectionTable(name = "appointment_request_alt_dates",
                     joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "decha_alterna")
    private List<LocalDate> fechaAlterna;
    
    @Column(length = 1000)
    private String razon;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReqStatus estado = ReqStatus.PENDIENTE;
    
    @Column(name = "fecha_creada", nullable = false, updatable = false)
    private LocalDateTime fechaCreada;
    
 // Cita generada si se acepta (1:1)
    @OneToOne(mappedBy = "peticion", cascade = CascadeType.ALL)
    private Cita cita;
    
    
    
    public PeticionCita() {
	super();
    }
    
    

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Paciente getPaciente() {
		return paciente;
	}



	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}



	public Doctor getDoctor() {
		return doctor;
	}



	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}



	public LocalDateTime getFechaPreferida() {
		return fechaPreferida;
	}



	public void setFechaPreferida(LocalDateTime fechaPreferida) {
		this.fechaPreferida = fechaPreferida;
	}



	public List<LocalDate> getFechaAlterna() {
		return fechaAlterna;
	}



	public void setFechaAlterna(List<LocalDate> fechaAlterna) {
		this.fechaAlterna = fechaAlterna;
	}



	public String getRazon() {
		return razon;
	}



	public void setRazon(String razon) {
		this.razon = razon;
	}



	public ReqStatus getEstado() {
		return estado;
	}



	public void setEstado(ReqStatus estado) {
		this.estado = estado;
	}



	public LocalDateTime getFechaCreada() {
		return fechaCreada;
	}



	public void setFechaCreada(LocalDateTime fechaCreada) {
		this.fechaCreada = fechaCreada;
	}



	public Cita getCita() {
		return cita;
	}



	public void setCita(Cita cita) {
		this.cita = cita;
	}



	@PrePersist
    protected void onCreate() {
        this.fechaCreada = LocalDateTime.now();
    }
 
    public enum ReqStatus {
        PENDIENTE, ACEPTADO, RECHAZADO
    }
    
    
}
