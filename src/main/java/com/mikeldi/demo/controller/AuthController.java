package com.mikeldi.demo.controller;

import com.mikeldi.demo.config.JwtService;
import com.mikeldi.demo.dto.LoginRequestDTO;
import com.mikeldi.demo.dto.LoginResponseDTO;
import com.mikeldi.demo.dto.RegisterDoctorDTO;
import com.mikeldi.demo.dto.RegisterPacienteDTO;
import com.mikeldi.demo.dto.DoctorProfileDTO;
import com.mikeldi.demo.dto.PacienteProfileDTO;
import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.model.Paciente;
import com.mikeldi.demo.model.Usuario;
import com.mikeldi.demo.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    
	public AuthController(AuthService authService, JwtService jwtService, AuthenticationManager authenticationManager,
			UserDetailsService userDetailsService) {
		super();
		this.authService = authService;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
	}
    
	// ── POST /api/auth/register/patient ───────────────────────────────────────
    @PostMapping("/register/patient")
    public ResponseEntity<PacienteProfileDTO> registerPatient(
            @RequestBody RegisterPacienteDTO dto) {
 
        Paciente patient = new Paciente();
        patient.setEmail(dto.getEmail());
        patient.setPassword(dto.getPassword());
        patient.setNombre(dto.getNombre());
        patient.setPhotoUrl(dto.getPhotoUrl());
        patient.setTelefono(dto.getTelefono());
        patient.setNumSeguro(dto.getNumSeguro());
        if (dto.getFechaNacimiento() != null)
            patient.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
 
        return ResponseEntity.ok(PacienteProfileDTO.from(authService.registerPatient(patient)));
    }
    
 // ── POST /api/auth/register/doctor ────────────────────────────────────────
    @PostMapping("/register/doctor")
    public ResponseEntity<DoctorProfileDTO> registerDoctor(
            @RequestBody RegisterDoctorDTO dto) {
 
        Doctor doctor = new Doctor();
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(dto.getPassword());
        doctor.setNombre(dto.getNombre());
        doctor.setPhotoUrl(dto.getPhotoUrl());
        doctor.setEspecialidad(dto.getEspecialidad());
        doctor.setLicenciaMedica(dto.getLicenciaMedica());
        doctor.setDocUrl(dto.getDocUrl());
 
        return ResponseEntity.ok(DoctorProfileDTO.from(authService.registerDoctor(doctor)));
    }
    
 // ── POST /api/auth/login ──────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtService.generateToken(userDetails);
        Usuario usuario = authService.findByEmail(dto.getEmail());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setRole(usuario.getRol().name());
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setEmail(usuario.getEmail());

        return ResponseEntity.ok(response);
    }

}
