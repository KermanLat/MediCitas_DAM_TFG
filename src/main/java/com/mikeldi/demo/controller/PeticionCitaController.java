package com.mikeldi.demo.controller;

import com.mikeldi.demo.dto.CitaDTO;
import com.mikeldi.demo.dto.PeticionCitaDTO;
import com.mikeldi.demo.dto.CrearPeticionDTO;
import com.mikeldi.demo.model.PeticionCita;
import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.model.Paciente;
import com.mikeldi.demo.service.PeticionCitaService;
import com.mikeldi.demo.service.DoctorService;
import com.mikeldi.demo.service.HistorialMedicoService;
import com.mikeldi.demo.service.PacienteService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class PeticionCitaController {
	
	private final PeticionCitaService requestService;
    private final DoctorService doctorService;
    private final PacienteService patientService;
    private final HistorialMedicoService recordService;
	public PeticionCitaController(PeticionCitaService requestService, DoctorService doctorService,
			PacienteService patientService, HistorialMedicoService recordService) {
		super();
		this.requestService = requestService;
		this.doctorService = doctorService;
		this.patientService = patientService;
		this.recordService = recordService;
	}
    
	// ── POST /api/requests ────────────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PeticionCitaDTO> create(
            @RequestBody CrearPeticionDTO dto) {
 
        Doctor doctor   = doctorService.findById(dto.getDoctorId());
        Paciente patient = patientService.findById(dto.getPacienteId());
 
        PeticionCita request = new PeticionCita();
        request.setDoctor(doctor);
        request.setPaciente(patient);
        request.setFechaPreferida(dto.getFechaPreferida());
        request.setFechaAlterna(dto.getFechasAlternas());
        request.setRazon(dto.getRazon());
 
        PeticionCita saved = requestService.create(request);
        return ResponseEntity.ok(PeticionCitaDTO.from(saved, getIllness(patient)));
    }
 
    // ── GET /api/requests/doctor/{doctorId}/pending ───────────────────────────
    @GetMapping("/doctor/{doctorId}/pending")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<PeticionCitaDTO>> getPendingByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(requestService.findPendingByDoctor(doctorId).stream()
            .map(r -> PeticionCitaDTO.from(r, getIllness(r.getPaciente())))
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/requests/patient/{patientId} ─────────────────────────────────
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<PeticionCitaDTO>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(requestService.findByPatient(patientId).stream()
            .map(r -> PeticionCitaDTO.from(r, getIllness(r.getPaciente())))
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/requests/patient/{patientId}/pending ─────────────────────────
    @GetMapping("/patient/{patientId}/pending")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<PeticionCitaDTO>> getPendingByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(requestService.findPendingByPatient(patientId).stream()
            .map(r -> PeticionCitaDTO.from(r, getIllness(r.getPaciente())))
            .collect(Collectors.toList()));
    }
 
    // ── PUT /api/requests/{id}/accept ─────────────────────────────────────────
    @PutMapping("/{id}/accept")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<CitaDTO> accept(@PathVariable Long id) {
        String illness = getIllness(requestService.findById(id).getPaciente());
        return ResponseEntity.ok(CitaDTO.from(requestService.accept(id, null, illness), illness));
    }
 
    // ── PUT /api/requests/{id}/reject ─────────────────────────────────────────
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PeticionCitaDTO> reject(@PathVariable Long id) {
        PeticionCita rejected = requestService.reject(id);
        return ResponseEntity.ok(PeticionCitaDTO.from(rejected, getIllness(rejected.getPaciente())));
    }
 
    // ── GET /api/requests/{id} ────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<PeticionCitaDTO> getById(@PathVariable Long id) {
        PeticionCita req = requestService.findById(id);
        return ResponseEntity.ok(PeticionCitaDTO.from(req, getIllness(req.getPaciente())));
    }
 
    // ── Helper ────────────────────────────────────────────────────────────────
    private String getIllness(Paciente patient) {
        try {
            return recordService.findByPatient(patient.getId()).getEnfermedadPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

}
