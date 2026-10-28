package com.mikeldi.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "sesion_notas")
public class SesionNota {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
 
    @ManyToOne(optional = false)
    @JoinColumn(name = "historial_id", nullable = false)
    private HistorialMedico historialMedico;
 
    // Cita en la que se escribió la nota (1:1)
    @OneToOne(optional = true) //sera, true, pq no he encontrado la manera de conectar cita con nota y el de que solo salga si expediente lo estas viendo 
    @JoinColumn(name = "cita_id", nullable = true) //se cambiara a false, mas adelante, pero por ahora no
    private Cita cita;
 
    @Column(nullable = false, length = 5000)
    private String contenido;
 
    @Column(name = "fecha_creada", nullable = false, updatable = false)
    private LocalDateTime fechaCreada;
 
    public SesionNota() {
        super();
    }
 
    @PrePersist
    protected void onCreate() {
        this.fechaCreada = LocalDateTime.now();
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Doctor getDoctor() {
        return doctor;
    }
 
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
 
    public HistorialMedico getHistorialMedico() {
        return historialMedico;
    }
 
    public void setHistorialMedico(HistorialMedico historialMedico) {
        this.historialMedico = historialMedico;
    }
 
    public Cita getCita() {
        return cita;
    }
 
    public void setCita(Cita cita) {
        this.cita = cita;
    }
 
    public String getContenido() {
        return contenido;
    }
 
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
 
    public LocalDateTime getFechaCreada() {
        return fechaCreada;
    }
 
    public void setFechaCreada(LocalDateTime fechaCreada) {
        this.fechaCreada = fechaCreada;
    }
}