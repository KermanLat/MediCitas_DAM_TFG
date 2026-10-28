package com.mikeldi.demo.controller;

import com.mikeldi.demo.dto.NotificacionDTO;
import com.mikeldi.demo.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificacionController {
	
	private final NotificacionService notificationService;

	public NotificacionController(NotificacionService notificationService) {
		super();
		this.notificationService = notificationService;
	}
	
	// ── GET /api/notifications/user/{userId} ──────────────────────────────────
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificacionDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findByUser(userId).stream()
            .map(NotificacionDTO::from)
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/notifications/user/{userId}/unseen ───────────────────────────
    @GetMapping("/user/{userId}/unseen")
    public ResponseEntity<List<NotificacionDTO>> getUnseen(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findUnseenByUser(userId).stream()
            .map(NotificacionDTO::from)
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/notifications/user/{userId}/count ────────────────────────────
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<?> countUnseen(@PathVariable Long userId) {
        return ResponseEntity.ok(Map.of("count", notificationService.countUnseen(userId)));
    }
 
    // ── PUT /api/notifications/{id}/seen ─────────────────────────────────────
    @PutMapping("/{id}/seen")
    public ResponseEntity<NotificacionDTO> markAsSeen(@PathVariable Long id) {
        return ResponseEntity.ok(NotificacionDTO.from(notificationService.markAsSeen(id)));
    }
 
    // ── PUT /api/notifications/user/{userId}/seen-all ─────────────────────────
    @PutMapping("/user/{userId}/seen-all")
    public ResponseEntity<?> markAllAsSeen(@PathVariable Long userId) {
        notificationService.markAllAsSeen(userId);
        return ResponseEntity.ok(Map.of("message", "Todas las notificaciones marcadas como vistas"));
    }
}
