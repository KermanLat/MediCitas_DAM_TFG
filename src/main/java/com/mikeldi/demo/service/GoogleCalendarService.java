package com.mikeldi.demo.service;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.mikeldi.demo.config.GoogleCalendarConfig;
import com.mikeldi.demo.model.Cita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleCalendarService {
	
	private static final Logger log = LoggerFactory.getLogger(GoogleCalendarService.class);
    private static final String APPLICATION_NAME = "TuApp";
    private static final String PRIMARY_CALENDAR = "primary";
    private static final ZoneId ZONE = ZoneId.of("Europe/Madrid");
    private final GoogleCalendarConfig googleCalendarConfig;

    public GoogleCalendarService(GoogleCalendarConfig googleCalendarConfig) {
        this.googleCalendarConfig = googleCalendarConfig;
    }
 
    // -------------------------------------------------------------------------
    // Construye el cliente de Calendar a partir del access token del doctor
    // -------------------------------------------------------------------------
    private Calendar buildCalendarService(String refreshToken)
            throws GeneralSecurityException, IOException {
    	
    	 String accessToken = getAccessToken(refreshToken);
    	
        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(accessToken);
 
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
 
    // -------------------------------------------------------------------------
    // Convierte LocalDateTime a DateTime de Google respetando Europe/Madrid
    // -------------------------------------------------------------------------
    private DateTime toGoogleDateTime(java.time.LocalDateTime ldt) {
        return new DateTime(java.util.Date.from(ldt.atZone(ZONE).toInstant()));
    }
    
    //algo para el acces
    
    private String getAccessToken(String refreshToken) throws IOException {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        
        String params = "refresh_token=" + refreshToken
                + "&client_id=" + googleCalendarConfig.getClientId()
                + "&client_secret=" + googleCalendarConfig.getClientSecret()
                + "&grant_type=refresh_token";

        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(params))
                .build();

        try {
            java.net.http.HttpResponse<String> response = java.net.http.HttpClient.newHttpClient()
                    .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            
            com.fasterxml.jackson.databind.JsonNode json = 
                    new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(response.body());
            return json.get("access_token").asText();
        } catch (InterruptedException e) {
            throw new IOException("Error al obtener access token", e);
        }
    }
 
    // -------------------------------------------------------------------------
    // Crea un evento en Google Calendar cuando se acepta una cita.
    // Devuelve el gcalEventId para guardarlo en la entidad Cita.
    //
    // Uso:
    //   String gcalId = googleCalendarService.createEvent(cita);
    //   cita.setGcalEventId(gcalId);
    //   citaRepository.save(cita);
    // -------------------------------------------------------------------------
    public String createEvent(Cita cita) {
        try {
            // El token OAuth2 del doctor debe estar guardado en la entidad Doctor
            Calendar service = buildCalendarService(
                    cita.getDoctor().getGcalToken());
 
            // Nombre del paciente para el resumen del evento
            String nombrePaciente = cita.getPaciente().getNombre();
 
            // Razón de la cita (viene de la PeticionCita)
            String razon = cita.getPeticion().getRazon();
 
            Event event = new Event()
                    .setSummary("Cita con " + nombrePaciente)
                    .setDescription("Motivo: " + (razon != null ? razon : "Sin especificar"))
                    .setLocation(cita.getSala())
                    .setAttendees(Arrays.asList(
                            new EventAttendee()
                                    .setEmail(cita.getPaciente().getEmail())
                                    .setDisplayName(nombrePaciente)))
                    .setStart(new EventDateTime()
                            .setDateTime(toGoogleDateTime(cita.getFechaProgramada()))
                            .setTimeZone(ZONE.getId()))
                    .setEnd(new EventDateTime()
                            .setDateTime(toGoogleDateTime(cita.getFechaProgramada().plusHours(1)))
                            .setTimeZone(ZONE.getId()));
 
            Event created = service.events()
                    .insert(PRIMARY_CALENDAR, event)
                    .setSendNotifications(true)  // avisa al paciente por email
                    .execute();
 
            log.info("Evento creado en Google Calendar: {} para cita {}",
                    created.getId(), cita.getId());
            return created.getId();
 
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error al crear evento en Google Calendar para cita {}: {}",
                    cita.getId(), e.getMessage());
            throw new RuntimeException("No se pudo crear el evento en Google Calendar", e);
        }
    }
 
    // -------------------------------------------------------------------------
    // Actualiza el evento cuando se cambia el horario de la cita.
    // -------------------------------------------------------------------------
    public void updateEvent(Cita cita) {
        try {
            String gcalEventId = cita.getGcalEventId();
            if (gcalEventId == null || gcalEventId.isBlank()) {
                log.warn("La cita {} no tiene gcalEventId; se omite la actualizacion.",
                        cita.getId());
                return;
            }
 
            Calendar service = buildCalendarService(
                    cita.getDoctor().getGcalToken());
 
            // Recuperamos el evento existente para no sobreescribir otros campos
            Event existing = service.events()
                    .get(PRIMARY_CALENDAR, gcalEventId)
                    .execute();
 
            String nombrePaciente = cita.getPaciente().getNombre();
 
            existing
                    .setSummary("Cita con " + nombrePaciente)
                    .setDescription("Motivo: " + cita.getPeticion().getRazon())
                    .setStart(new EventDateTime()
                            .setDateTime(toGoogleDateTime(cita.getFechaProgramada()))
                            .setTimeZone(ZONE.getId()))
                    .setEnd(new EventDateTime()
                            .setDateTime(toGoogleDateTime(cita.getFechaProgramada().plusHours(1)))
                            .setTimeZone(ZONE.getId()));
 
            service.events()
                    .update(PRIMARY_CALENDAR, gcalEventId, existing)
                    .setSendNotifications(true)
                    .execute();
 
            log.info("Evento {} actualizado en Google Calendar.", gcalEventId);
 
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error al actualizar evento en Google Calendar para cita {}: {}",
                    cita.getId(), e.getMessage());
            throw new RuntimeException("No se pudo actualizar el evento en Google Calendar", e);
        }
    }
 
    // -------------------------------------------------------------------------
    // Elimina el evento cuando se cancela la cita.
    //
    // Uso:
    //   googleCalendarService.deleteEvent(cita.getGcalEventId(), cita.getDoctor().getGcalToken());
    // -------------------------------------------------------------------------
    public void deleteEvent(String gcalEventId, String gcalToken) {
        if (gcalEventId == null || gcalEventId.isBlank()) {
            log.warn("gcalEventId vacio; no se elimina nada.");
            return;
        }
        try {
            Calendar service = buildCalendarService(gcalToken);
 
            service.events()
                    .delete(PRIMARY_CALENDAR, gcalEventId)
                    .execute();
 
            log.info("Evento {} eliminado de Google Calendar.", gcalEventId);
 
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error al eliminar evento {} de Google Calendar: {}",
                    gcalEventId, e.getMessage());
            throw new RuntimeException("No se pudo eliminar el evento en Google Calendar", e);
        }
    }
 
    // -------------------------------------------------------------------------
    // Obtiene los eventos de un doctor en un rango de tiempo.
    // timeMin / timeMax en formato RFC 3339: "2025-05-01T00:00:00Z"
    // -------------------------------------------------------------------------
    public List<Event> getEvents(String gcalToken, String timeMin, String timeMax) {
        try {
            Calendar service = buildCalendarService(gcalToken);
 
            Events result = service.events()
                    .list(PRIMARY_CALENDAR)
                    .setTimeMin(new DateTime(timeMin))
                    .setTimeMax(new DateTime(timeMax))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)  // expande eventos recurrentes
                    .execute();
 
            List<Event> events = result.getItems();
            log.info("Se obtuvieron {} eventos de Google Calendar.", events.size());
            return events;
 
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error al obtener eventos de Google Calendar: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener los eventos de Google Calendar", e);
        }
    }
}
