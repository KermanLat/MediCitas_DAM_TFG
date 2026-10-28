package com.mikeldi.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
 
@Entity
@Table(name = "historial_medicos")
public class HistorialMedico {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    // 1:1 con Paciente
    @OneToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;
 
    @Column(name = "enfermedad_principal")
    private String enfermedadPrincipal;
 
    @Column(length = 1000)
    private String alergias;
 
    @Column(name = "medicamentos_act", length = 1000)
    private String medicamentosActuales;
 
    @Column(name = "tipo_sangre", length = 5)
    private String tipoSangre;
 
    @Column(name = "fecha_actualizado")
    private LocalDateTime fechaActualizado;
 
    // Notas de sesión (1:N)
    @OneToMany(mappedBy = "historialMedico", cascade = CascadeType.ALL)
    private List<SesionNota> sesionNotas;
 
    // Doctores que gestionan este expediente (N:M)
    @ManyToMany(mappedBy = "managedRecords")
    private List<Doctor> doctores;
 
    public HistorialMedico() {
        super();
    }
 
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizado = LocalDateTime.now();
    }
 
    @PrePersist
    protected void onCreate() {
        this.fechaActualizado = LocalDateTime.now();
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
 
    public String getEnfermedadPrincipal() {
        return enfermedadPrincipal;
    }
 
    public void setEnfermedadPrincipal(String enfermedadPrincipal) {
        this.enfermedadPrincipal = enfermedadPrincipal;
    }
 
    public String getAlergias() {
        return alergias;
    }
 
    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }
 
    public String getMedicamentosActuales() {
        return medicamentosActuales;
    }
 
    public void setMedicamentosActuales(String medicamentosActuales) {
        this.medicamentosActuales = medicamentosActuales;
    }
 
    public String getTipoSangre() {
        return tipoSangre;
    }
 
    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }
 
    public LocalDateTime getFechaActualizado() {
        return fechaActualizado;
    }
 
    public void setFechaActualizado(LocalDateTime fechaActualizado) {
        this.fechaActualizado = fechaActualizado;
    }
 
    public List<SesionNota> getSesionNotas() {
        return sesionNotas;
    }
 
    public void setSesionNotas(List<SesionNota> sesionNotas) {
        this.sesionNotas = sesionNotas;
    }
 
    public List<Doctor> getDoctores() {
        return doctores;
    }
 
    public void setDoctores(List<Doctor> doctores) {
        this.doctores = doctores;
    }
}