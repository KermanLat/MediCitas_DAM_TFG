package com.mikeldi.demo.controller;

import com.mikeldi.demo.dto.CitaDTO;
import com.mikeldi.demo.dto.RescheduleDTO;
import com.mikeldi.demo.model.Cita;
import com.mikeldi.demo.service.CitaService;
import com.mikeldi.demo.service.HistorialMedicoService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class CitaController {
	private final CitaService appointmentService;
    private final HistorialMedicoService recordService;
	public CitaController(CitaService appointmentService, HistorialMedicoService recordService) {
		super();
		this.appointmentService = appointmentService;
		this.recordService = recordService;
	}
    
	// ── GET /api/appointments/{id} ────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> getById(@PathVariable Long id) {
        Cita appt = appointmentService.findById(id);
        return ResponseEntity.ok(CitaDTO.from(appt, getIllness(appt)));
    }
 
    // ── GET /api/appointments/doctor/{doctorId}/today ─────────────────────────
    @GetMapping("/doctor/{doctorId}/today")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<CitaDTO>> getTodayByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.findTodayByDoctor(doctorId).stream()
            .map(a -> CitaDTO.from(a, getIllness(a)))
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/appointments/patient/{patientId}/upcoming ────────────────────
    @GetMapping("/patient/{patientId}/upcoming")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<CitaDTO>> getUpcomingByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.findUpcomingByPatient(patientId).stream()
            .map(a -> CitaDTO.from(a, getIllness(a)))
            .collect(Collectors.toList()));
    }
 
    // ── PUT /api/appointments/{id}/reschedule ─────────────────────────────────
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<CitaDTO> reschedule(
            @PathVariable Long id,
            @RequestBody RescheduleDTO dto) {
        Cita appt = appointmentService.reschedule(id, dto.getFechaProgramada());
        return ResponseEntity.ok(CitaDTO.from(appt, getIllness(appt)));
    }
 
    // ── PUT /api/appointments/{id}/cancel ─────────────────────────────────────
    @PutMapping("/{id}/cancel")
    public ResponseEntity<CitaDTO> cancel(@PathVariable Long id) {
        Cita appt = appointmentService.cancel(id);
        return ResponseEntity.ok(CitaDTO.from(appt, getIllness(appt)));
    }
 
    // ── PUT /api/appointments/{id}/complete ───────────────────────────────────
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<CitaDTO> complete(@PathVariable Long id) {
        Cita appt = appointmentService.complete(id);
        return ResponseEntity.ok(CitaDTO.from(appt, getIllness(appt)));
    }
 
    // ── Helper ────────────────────────────────────────────────────────────────
    private String getIllness(Cita appt) {
        try {
            return recordService.findByPatient(appt.getPaciente().getId()).getEnfermedadPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

}
