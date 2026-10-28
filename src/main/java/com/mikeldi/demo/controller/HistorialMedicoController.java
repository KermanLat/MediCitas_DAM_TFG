package com.mikeldi.demo.controller;


import com.mikeldi.demo.dto.HistorialMedicoDTO;
import com.mikeldi.demo.model.HistorialMedico;
import com.mikeldi.demo.service.HistorialMedicoService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
public class HistorialMedicoController {
	
	private final HistorialMedicoService recordService;

	public HistorialMedicoController(HistorialMedicoService recordService) {
		super();
		this.recordService = recordService;
	}
	
	// ── GET /api/medical-records/patient/{patientId} ──────────────────────────
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<HistorialMedicoDTO> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(HistorialMedicoDTO.from(recordService.findByPatient(patientId)));
    }
 
    // ── PUT /api/medical-records/patient/{patientId} ──────────────────────────
    @PutMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<HistorialMedicoDTO> update(
            @PathVariable Long patientId,
            @RequestBody HistorialMedico updated) {
        return ResponseEntity.ok(HistorialMedicoDTO.from(recordService.update(patientId, updated)));
    }
 
    // ── POST /api/medical-records/patient/{patientId}/doctor/{doctorId} ───────
    @PostMapping("/patient/{patientId}/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<HistorialMedicoDTO> addDoctor(
            @PathVariable Long patientId,
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(HistorialMedicoDTO.from(recordService.addDoctor(patientId, doctorId)));
    }
 
    // ── DELETE /api/medical-records/patient/{patientId}/doctor/{doctorId} ─────
    @DeleteMapping("/patient/{patientId}/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<HistorialMedicoDTO> removeDoctor(
            @PathVariable Long patientId,
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(HistorialMedicoDTO.from(recordService.removeDoctor(patientId, doctorId)));
    }
}
