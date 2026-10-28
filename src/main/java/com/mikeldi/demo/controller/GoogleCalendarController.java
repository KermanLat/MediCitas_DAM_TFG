package com.mikeldi.demo.controller;

import com.mikeldi.demo.config.GoogleCalendarConfig;
import com.mikeldi.demo.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import com.mikeldi.demo.model.Doctor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
 
import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleCalendarController {
	
	private final GoogleCalendarConfig googleCalendarConfig;
    private final DoctorService doctorService;
	public GoogleCalendarController(GoogleCalendarConfig googleCalendarConfig, DoctorService doctorService) {
		super();
		this.googleCalendarConfig = googleCalendarConfig;
		this.doctorService = doctorService;
	}
    
	// ── GET /api/auth/google/authorize ────────────────────────────────────────
    // Devuelve la URL a la que el doctor debe ir para autorizar Google Calendar
    @GetMapping("/authorize")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getAuthorizationUrl() {
        return ResponseEntity.ok(Map.of(
            "authorizationUrl", googleCalendarConfig.buildAuthorizationUrl()
        ));
    }
 
    // ── GET /api/auth/google/callback ─────────────────────────────────────────
    // Google redirige aquí tras la autorización del doctor
    @GetMapping("/callback")
    public ResponseEntity<?> handleCallback(
            @RequestParam String code,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            // 1. Intercambiar el code por access_token + refresh_token
            String tokenUrl = "https://oauth2.googleapis.com/token";

            String params = "code=" + code
                    + "&client_id=" + googleCalendarConfig.getClientId()
                    + "&client_secret=" + googleCalendarConfig.getClientSecret()
                    + "&redirect_uri=" + googleCalendarConfig.getRedirectUri()
                    + "&grant_type=authorization_code";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // 2. Extraer el refresh_token del JSON de respuesta
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());
            String refreshToken = json.get("refresh_token").asText();

            // 3. Guardarlo en el doctor autenticado
            Doctor doctor = doctorService.findByEmail(userDetails.getUsername());
            doctorService.saveGcalToken(doctor.getId(), refreshToken);

            // 4. Redirigir al calendario
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "/doctor/calendar")
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "/doctor/calendar?error=gcal")
                    .build();
        }
    }
    
    // ── POST /api/auth/google/disconect─────────────────────────────────────────
    // Google desconecta el calendar
    @PostMapping("/disconnect")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> disconnect(@AuthenticationPrincipal UserDetails userDetails) {
        Doctor doctor = doctorService.findByEmail(userDetails.getUsername());
        doctorService.saveGcalToken(doctor.getId(), null);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/doctor/calendar")
                .build();
    }
}
