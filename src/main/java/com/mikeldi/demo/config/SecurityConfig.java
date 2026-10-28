package com.mikeldi.demo.config;

import com.mikeldi.demo.model.Usuario;
import com.mikeldi.demo.repository.UsuarioRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Lazy;
 
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final UsuarioRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
	public SecurityConfig(UsuarioRepository userRepository, 
			@Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
		super();
		this.userRepository = userRepository;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
    
	// ── Rutas públicas (no requieren autenticación) ────────────────────────────
    private static final String[] PUBLIC_URLS = {
        "/api/auth/**",          // login y registro
        "/api/auth/register/**", // registro doctor y paciente
        "/swagger-ui/**",        // documentación (opcional)
        "/v3/api-docs/**",
        "/login",
        "/register/**",      // ← añade esto
        "/css/**",           // ← recursos estáticos
        "/js/**",
        "/images/**",
        "/error"          // ← añade esto
    };
 
    // ── Rutas solo para doctores ───────────────────────────────────────────────
    private static final String[] DOCTOR_URLS = {
        "/api/doctor/**",
        "/api/appointments/doctor/**",
        "/api/requests/doctor/**",
        "/api/medical-records/**",
        "/api/session-notes/**",
        "/api/messages/**"
    };
 
    // ── Rutas solo para pacientes ──────────────────────────────────────────────
    private static final String[] PATIENT_URLS = {
        "/api/patient/**",
        "/api/appointments/patient/**",
        "/api/requests/patient/**"
    };
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
            	    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // ← cambiar esto, separa la api de thymeleaf
            	)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(DOCTOR_URLS).hasRole("DOCTOR")
                .requestMatchers(PATIENT_URLS).hasRole("PATIENT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form                                    // ← añadir aquí
                    .loginPage("/login")
                    .defaultSuccessUrl("/redirect-after-login", true)
                    .permitAll()
                )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);
 
        return http.build();
    }
 
    // ── UserDetailsService: carga el usuario desde la BD por email ─────────────
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Usuario user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "Usuario no encontrado: " + email
                ));
 
            // Convierte el Role del usuario en un GrantedAuthority para Spring Security
            return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(
                    "ROLE_" + user.getRol().name()  // ROLE_DOCTOR o ROLE_PATIENT
                )))
                .build();
        };
    }
 
    // ── AuthenticationProvider: conecta UserDetailsService con PasswordEncoder ─
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
 
    // ── AuthenticationManager: necesario para el login manual en AuthService ───
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
 
    // ── BCrypt para encriptar contraseñas ──────────────────────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
