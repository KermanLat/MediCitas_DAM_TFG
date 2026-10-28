package com.mikeldi.demo.config;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
 
import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class GoogleCalendarConfig {
	
	@Value("${google.calendar.app-name}")
    private String appName;
 
    @Value("${google.calendar.client-id}")
    private String clientId;
 
    @Value("${google.calendar.client-secret}")
    private String clientSecret;
 
    @Value("${google.calendar.redirect-uri}")
    private String redirectUri;
 
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
 
    // ── Construir cliente de Google Calendar con el token del doctor ──────────
    // Se llama desde GoogleCalendarService pasando el gcalToken del doctor
    public Calendar buildCalendarService(String accessToken)
            throws GeneralSecurityException, IOException {
 
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
 
        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(accessToken);
 
        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(appName)
                .build();
    }
 
    // ── URL de autorización OAuth2 que el doctor debe visitar ─────────────────
    // El doctor visita esta URL, acepta los permisos, y Google redirige
    // a redirect-uri con un código que luego intercambiamos por el token
    public String buildAuthorizationUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=" + CalendarScopes.CALENDAR
                + "&access_type=offline"   // para obtener refresh_token
                + "&prompt=consent";
    }
 
    // Getters para usar en otros componentes si hace falta
    public String getClientId()     { return clientId; }
    public String getClientSecret() { return clientSecret; }
    public String getRedirectUri()  { return redirectUri; }
    public String getAppName()      { return appName; }

}
