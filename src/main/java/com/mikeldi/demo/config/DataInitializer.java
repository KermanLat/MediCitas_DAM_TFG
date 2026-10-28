package com.mikeldi.demo.config;

import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.model.Usuario;
import com.mikeldi.demo.repository.DoctorRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.doctor-email:}")
    private String seedEmail;

    @Value("${app.seed.doctor-password:}")
    private String seedPassword;

    public DataInitializer(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Solo se crea un doctor inicial si se definen ambas variables de entorno
        if (seedEmail.isBlank() || seedPassword.isBlank()) {
            return;
        }
        String email = seedEmail;

        // Solo crea el doctor si no existe ya en la base de datos
        if (doctorRepository.findByEmail(email).isEmpty()) {
            Doctor doctor = new Doctor();
            doctor.setEmail(email);
            doctor.setPassword(passwordEncoder.encode(seedPassword));
            doctor.setNombre("Doctor inicial");
            doctor.setRol(Usuario.Rol.DOCTOR);
            doctor.setVerificado(true); // validado = true

            doctorRepository.save(doctor);
            System.out.println("✅ Doctor inicial creado: " + email);
        } else {
            System.out.println("ℹ️ El doctor ya existe, no se ha creado de nuevo.");
        }
    }
}
