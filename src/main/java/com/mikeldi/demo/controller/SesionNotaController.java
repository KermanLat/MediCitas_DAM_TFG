package com.mikeldi.demo.controller;

import com.mikeldi.demo.dto.SesionNotaDTO;
import com.mikeldi.demo.model.SesionNota;
import com.mikeldi.demo.service.SesionNotaService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 

@RestController
@RequestMapping("/api/session-notes")
public class SesionNotaController {
	
	private final SesionNotaService noteService;

	public SesionNotaController(SesionNotaService noteService) {
		super();
		this.noteService = noteService;
	}
	
	// ── POST /api/session-notes ───────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<SesionNotaDTO> create(@RequestBody SesionNota note) {
        return ResponseEntity.ok(SesionNotaDTO.from(noteService.create(note)));
    }
 
    // ── GET /api/session-notes/record/{recordId} ──────────────────────────────
    @GetMapping("/record/{recordId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<SesionNotaDTO>> getByMedicalRecord(
            @PathVariable Long recordId) {
        return ResponseEntity.ok(noteService.findByMedicalRecord(recordId).stream()
            .map(SesionNotaDTO::from)
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/session-notes/appointment/{appointmentId} ────────────────────
    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<SesionNotaDTO>> getByAppointment(
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(noteService.findByAppointment(appointmentId).stream()
            .map(SesionNotaDTO::from)
            .collect(Collectors.toList()));
    }
 
    // ── PUT /api/session-notes/{id} ───────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<SesionNotaDTO> update(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(SesionNotaDTO.from(noteService.update(id, body.get("content"))));
    }
 
    // ── DELETE /api/session-notes/{id} ────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Nota eliminada correctamente"));
    }
}
