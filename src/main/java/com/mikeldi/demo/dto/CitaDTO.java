package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.Cita;
import java.time.LocalDateTime;

public class CitaDTO {
	
	private Long id;
    private LocalDateTime fechaProgramada;
    private String sala;
    private String estado;
    private String gcalEventId;
 
    // Info resumida del doctor y paciente
    private DoctorSummaryDTO doctor;
    private PacienteSummaryDTO paciente;
 
    public CitaDTO() {
    }
 
    public CitaDTO(Long id, LocalDateTime fechaProgramada, String sala, String estado,
                   String gcalEventId, DoctorSummaryDTO doctor, PacienteSummaryDTO paciente) {
        this.id = id;
        this.fechaProgramada = fechaProgramada;
        this.sala = sala;
        this.estado = estado;
        this.gcalEventId = gcalEventId;
        this.doctor = doctor;
        this.paciente = paciente;
    }
 
    public static CitaDTO from(Cita cita, String enfermedadPrincipal) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setFechaProgramada(cita.getFechaProgramada());
        dto.setSala(cita.getSala());
        dto.setEstado(cita.getEstado().name());
        dto.setGcalEventId(cita.getGcalEventId());
        dto.setDoctor(DoctorSummaryDTO.from(cita.getDoctor()));
        dto.setPaciente(PacienteSummaryDTO.from(cita.getPaciente(), enfermedadPrincipal));
        return dto;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }
 
    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }
 
    public String getSala() {
        return sala;
    }
 
    public void setSala(String sala) {
        this.sala = sala;
    }
 
    public String getEstado() {
        return estado;
    }
 
    public void setEstado(String estado) {
        this.estado = estado;
    }
 
    public String getGcalEventId() {
        return gcalEventId;
    }
 
    public void setGcalEventId(String gcalEventId) {
        this.gcalEventId = gcalEventId;
    }
 
    public DoctorSummaryDTO getDoctor() {
        return doctor;
    }
 
    public void setDoctor(DoctorSummaryDTO doctor) {
        this.doctor = doctor;
    }
 
    public PacienteSummaryDTO getPaciente() {
        return paciente;
    }
 
    public void setPaciente(PacienteSummaryDTO paciente) {
        this.paciente = paciente;
    }
    
	
	
    

}
