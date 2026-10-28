package com.mikeldi.demo.controller;

import com.mikeldi.demo.dto.PacienteProfileDTO;
import com.mikeldi.demo.dto.PacienteSummaryDTO;
import com.mikeldi.demo.model.Paciente;
import com.mikeldi.demo.service.HistorialMedicoService;
import com.mikeldi.demo.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patient")
public class PacienteController {
	
	private final PacienteService patientService;
    private final HistorialMedicoService recordService;
    
	public PacienteController(PacienteService patientService, HistorialMedicoService recordService) {
		super();
		this.patientService = patientService;
		this.recordService = recordService;
	}
    
	// ── GET /api/patient/{id} ─────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<PacienteProfileDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(PacienteProfileDTO.from(patientService.findById(id)));
    }
 
    // ── GET /api/patient/all ──────────────────────────────────────────────────
    // Devuelve lista resumida con nombre, edad y enfermedad (cuadrícula del doctor)
    @GetMapping("/all")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<PacienteSummaryDTO>> getAll() {
    	return ResponseEntity.ok(patientService.findAll().stream()
    	        .map(p -> {
    	            String enfermedad = getMainIllness(p); //este getMainIllness es de una funcion de aqui (abajo)
    	            return PacienteSummaryDTO.from(p, enfermedad);
    	        })
    	        .collect(Collectors.toList()));
    	}
 
    // ── GET /api/patient/search?name=xxx ─────────────────────────────────────
    @GetMapping("/search")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<PacienteSummaryDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(patientService.searchByName(name).stream()
            .map(p -> PacienteSummaryDTO.from(p, getMainIllness(p))) //este getMainIllness es de una funcion de aqui (abajo)
            .collect(Collectors.toList()));
    }
 
    // ── PUT /api/patient/{id} ─────────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PacienteProfileDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody Paciente updated) {
        return ResponseEntity.ok(PacienteProfileDTO.from(patientService.updateProfile(id, updated)));
    }
 
    // ── PUT /api/patient/{id}/discharge ──────────────────────────────────────
    @PutMapping("/{id}/discharge")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> discharge(@PathVariable Long id) {
        patientService.discharge(id);
        return ResponseEntity.ok(Map.of("message", "Paciente dado de alta correctamente"));
    }
 
    // ── Helper: obtener la enfermedad principal del expediente ────────────────
    private String getMainIllness(Paciente p) {
        try {
            return recordService.findByPatient(p.getId()).getEnfermedadPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
    

}
