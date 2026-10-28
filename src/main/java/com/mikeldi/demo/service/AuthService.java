package com.mikeldi.demo.service;

import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.model.Paciente;
import com.mikeldi.demo.model.Usuario;
import com.mikeldi.demo.repository.DoctorRepository;
import com.mikeldi.demo.repository.PacienteRepository;
import com.mikeldi.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
@Service
public class AuthService {
	private final UsuarioRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PacienteRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    
    
    public AuthService(
            UsuarioRepository userRepository,
            DoctorRepository doctorRepository,
            PacienteRepository patientRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
 // Registro de un nuevo paciente
    @Transactional
    public Paciente registerPatient(Paciente patient) {
        if (userRepository.existsByEmail(patient.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setRol(Usuario.Rol.PACIENTE);
        return patientRepository.save(patient);
    }
    
 // Registro de un nuevo doctor (queda pendiente de verificación)
    @Transactional
    public Doctor registerDoctor(Doctor doctor) {
        if (userRepository.existsByEmail(doctor.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor.setRol(Usuario.Rol.DOCTOR);
        doctor.setVerificado(false); // pendiente de verificación
        return doctorRepository.save(doctor);
    }
 
    // Buscar usuario por email (usado por Spring Security)
    public Usuario findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
    
 // Verificar si las credenciales son correctas
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    

}
