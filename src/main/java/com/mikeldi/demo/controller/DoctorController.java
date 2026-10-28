package com.mikeldi.demo.controller;

import com.mikeldi.demo.dto.DoctorProfileDTO;
import com.mikeldi.demo.dto.DoctorSummaryDTO;
import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
	
	private final DoctorService doctorService;

	public DoctorController(DoctorService doctorService) {
		super();
		this.doctorService = doctorService;
	}
	
	// ── GET /api/doctor/{id} ──────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<DoctorProfileDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(DoctorProfileDTO.from(doctorService.findById(id)));
    }
    
 // ── GET /api/doctor/all ───────────────────────────────────────────────────
    @GetMapping("/all")
    public ResponseEntity<List<DoctorSummaryDTO>> getAllVerified() {
        return ResponseEntity.ok(doctorService.findAllVerified().stream()
            .map(DoctorSummaryDTO::from)
            .collect(Collectors.toList()));
    }
    
 // ── GET /api/doctor/search?name=xxx ──────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<List<DoctorSummaryDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchByName(name).stream()
            .map(DoctorSummaryDTO::from)
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/doctor/specialty?name=xxx ───────────────────────────────────
    @GetMapping("/specialty")
    public ResponseEntity<List<DoctorSummaryDTO>> getBySpecialty(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.findBySpecialty(name).stream()
            .map(DoctorSummaryDTO::from)
            .collect(Collectors.toList()));
    }
    
    // ── PUT /api/doctor/{id} ──────────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorProfileDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody Doctor updated) {
        return ResponseEntity.ok(DoctorProfileDTO.from(doctorService.updateProfile(id, updated)));
    }
    
 // ── PUT /api/doctor/{id}/verify ───────────────────────────────────────────
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorProfileDTO> verifyDoctor(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails caller) {
        requireVerifiedDoctor(caller);
        return ResponseEntity.ok(DoctorProfileDTO.from(doctorService.verifyDoctor(id)));
    }
 
    // ── GET /api/doctor/pending ───────────────────────────────────────────────
    @GetMapping("/pending")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<DoctorProfileDTO>> getPendingVerification(
            @AuthenticationPrincipal UserDetails caller) {
        requireVerifiedDoctor(caller);
        return ResponseEntity.ok(doctorService.findPendingVerification().stream()
            .map(DoctorProfileDTO::from)
            .collect(Collectors.toList()));
    }
 
    // ── POST /api/doctor/{id}/gcal-token ─────────────────────────────────────
    @PostMapping("/{id}/gcal-token")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> saveGcalToken(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        doctorService.saveGcalToken(id, body.get("token"));
        return ResponseEntity.ok(Map.of("message", "Token de Google Calendar guardado"));
    }


    // Solo un doctor ya verificado puede verificar a otros o ver los pendientes
    private void requireVerifiedDoctor(UserDetails caller) {
        Doctor doctor = doctorService.findByEmail(caller.getUsername());
        if (!Boolean.TRUE.equals(doctor.getVerificado())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Solo un doctor verificado puede realizar esta acción");
        }
    }
}
