package com.mikeldi.demo.controller;

import com.mikeldi.demo.dto.MensajeDTO;
import com.mikeldi.demo.service.MensajeService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MensajeController {
	
	private final MensajeService messageService;

	public MensajeController(MensajeService messageService) {
		super();
		this.messageService = messageService;
	}
	
	
	// ── POST /api/messages ────────────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MensajeDTO> send(@RequestBody Map<String, Object> body) {
        Long senderId   = Long.valueOf(body.get("senderId").toString());
        Long receiverId = Long.valueOf(body.get("receiverId").toString());
        String content  = body.get("content").toString();
        return ResponseEntity.ok(MensajeDTO.from(messageService.send(senderId, receiverId, content)));
    }
 
    // ── GET /api/messages/conversation?doctorA=1&doctorB=2 ───────────────────
    @GetMapping("/conversation")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<MensajeDTO>> getConversation(
            @RequestParam Long doctorA,
            @RequestParam Long doctorB) {
        return ResponseEntity.ok(messageService.findConversation(doctorA, doctorB).stream()
            .map(MensajeDTO::from)
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/messages/received/{doctorId} ─────────────────────────────────
    @GetMapping("/received/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<MensajeDTO>> getReceived(@PathVariable Long doctorId) {
        return ResponseEntity.ok(messageService.findReceived(doctorId).stream()
            .map(MensajeDTO::from)
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/messages/unread/{doctorId} ───────────────────────────────────
    @GetMapping("/unread/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<MensajeDTO>> getUnread(@PathVariable Long doctorId) {
        return ResponseEntity.ok(messageService.findUnread(doctorId).stream()
            .map(MensajeDTO::from)
            .collect(Collectors.toList()));
    }
 
    // ── GET /api/messages/unread/{doctorId}/count ─────────────────────────────
    @GetMapping("/unread/{doctorId}/count")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> countUnread(@PathVariable Long doctorId) {
        return ResponseEntity.ok(Map.of("count", messageService.countUnread(doctorId)));
    }
 
    // ── PUT /api/messages/{id}/read ───────────────────────────────────────────
    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MensajeDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(MensajeDTO.from(messageService.markAsRead(id)));
    }
 
    // ── PUT /api/messages/conversation/read ──────────────────────────────────
    @PutMapping("/conversation/read")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> markConversationAsRead(
            @RequestParam Long receiverId,
            @RequestParam Long senderId) {
        messageService.markConversationAsRead(receiverId, senderId);
        return ResponseEntity.ok(Map.of("message", "Conversación marcada como leída"));
    }

}
