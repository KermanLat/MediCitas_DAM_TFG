package com.mikeldi.demo.dto;

import com.mikeldi.demo.model.HistorialMedico;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HistorialMedicoDTO {
	
	private Long            id;
    private String          enfermedadPrincipal;
    private String          alergias;
    private String          medicamentosActuales;
    private String          tipoSangre;
    private LocalDateTime   fechaActualizado;


    // Lista de notas resumidas del expediente
    private List<SesionNotaDTO> sesionNotas;
 
    // Doctores que gestionan el expediente
    private List<DoctorSummaryDTO> doctores;
    
    public HistorialMedicoDTO() {
    }
    

    public HistorialMedicoDTO(Long id, String enfermedadPrincipal, String alergias, String medicamentosActuales,
			String tipoSangre, LocalDateTime fechaActualizado, List<SesionNotaDTO> sesionNotas,
			List<DoctorSummaryDTO> doctores) {
		super();
		this.id = id;
		this.enfermedadPrincipal = enfermedadPrincipal;
		this.alergias = alergias;
		this.medicamentosActuales = medicamentosActuales;
		this.tipoSangre = tipoSangre;
		this.fechaActualizado = fechaActualizado;
		this.sesionNotas = sesionNotas;
		this.doctores = doctores;
	}


	public static HistorialMedicoDTO from(HistorialMedico historial) {
        HistorialMedicoDTO dto = new HistorialMedicoDTO();
        dto.setId(historial.getId());
        dto.setEnfermedadPrincipal(historial.getEnfermedadPrincipal());
        dto.setAlergias(historial.getAlergias());
        dto.setMedicamentosActuales(historial.getMedicamentosActuales());
        dto.setTipoSangre(historial.getTipoSangre());
        dto.setFechaActualizado(historial.getFechaActualizado());
        
        dto.setSesionNotas(historial.getSesionNotas().stream()
                .map(SesionNotaDTO::from)
                .collect(Collectors.toList()));
        
        dto.setDoctores(historial.getDoctores().stream()
                .map(DoctorSummaryDTO::from)
                .collect(Collectors.toList()));
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<SesionNotaDTO> getSesionNotas() {
        return sesionNotas;
    }

    public void setSesionNotas(List<SesionNotaDTO> sesionNotas) {
        this.sesionNotas = sesionNotas;
    }

    public List<DoctorSummaryDTO> getDoctores() {
        return doctores;
    }

    public void setDoctores(List<DoctorSummaryDTO> doctores) {
        this.doctores = doctores;
    }

    
}
