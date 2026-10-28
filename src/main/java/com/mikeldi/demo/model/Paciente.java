package com.mikeldi.demo.model;

import java.time.LocalDate;
import java.util.List;
 
import jakarta.persistence.*;
 
@Entity
@Table(name = "pacientes")
@PrimaryKeyJoinColumn(name = "user_id")
public class Paciente extends Usuario {
 
    @Column(name = "fecha_nac")
    private LocalDate fechaNacimiento;
 
    private String telefono;
 
    @Column(name = "num_seguro")
    private String numSeguro;
 
    // Relaciones
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL)
    private HistorialMedico historialMedico;
 
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<PeticionCita> peticionCitas;
 
    public Paciente() {
        super();
    }
 
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
 
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
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
 
    public HistorialMedico getHistorialMedico() {
        return historialMedico;
    }
 
    public void setHistorialMedico(HistorialMedico historialMedico) {
        this.historialMedico = historialMedico;
    }
 
    public List<PeticionCita> getPeticionCitas() {
        return peticionCitas;
    }
 
    public void setPeticionCitas(List<PeticionCita> peticionCitas) {
        this.peticionCitas = peticionCitas;
    }
}