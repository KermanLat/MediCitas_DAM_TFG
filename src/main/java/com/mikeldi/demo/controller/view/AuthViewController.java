package com.mikeldi.demo.controller.view;

import com.mikeldi.demo.dto.RegisterDoctorDTO;
import com.mikeldi.demo.dto.RegisterPacienteDTO;
import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.model.Paciente;
import com.mikeldi.demo.service.AuthService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.time.LocalDate;

@Controller  // ← @Controller en vez de @RestController para devolver vistas HTML
public class AuthViewController {

    private final AuthService authService;
    

    public AuthViewController(AuthService authService) {
		super();
		this.authService = authService;
	}

	// ── GET /login ────────────────────────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";  // → templates/auth/login.html
    }

    // ── GET /register/patient ─────────────────────────────────────────────────
    @GetMapping("/register/patient")
    public String registerPatientPage(Model model) {
        model.addAttribute("registerPatientDTO", new RegisterPacienteDTO());
        return "auth/register-patient";
    }

    // ── POST /register/patient ────────────────────────────────────────────────
    @PostMapping("/register/patient")
    public String registerPatient(
            @Valid @ModelAttribute RegisterPacienteDTO dto,
            BindingResult result,
            @RequestParam String confirmPassword,
            Model model) {

        // Validar que las contraseñas coinciden
        if (!dto.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "auth/register-patient";
        }

        if (result.hasErrors()) {
            return "auth/register-patient";
        }

        try {
            Paciente patient = new Paciente();
            patient.setEmail(dto.getEmail());
            patient.setPassword(dto.getPassword());
            patient.setNombre(dto.getNombre());
            patient.setTelefono(dto.getTelefono());
            patient.setNumSeguro(dto.getNumSeguro());
            if (dto.getFechaNacimiento() != null)
                patient.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));

            authService.registerPatient(patient);

            // Redirige al login con mensaje de éxito
            return "redirect:/login?registered";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register-patient";
        }
    }

    // ── GET /register/doctor ──────────────────────────────────────────────────
    @GetMapping("/register/doctor")
    public String registerDoctorPage(Model model) {
        model.addAttribute("registerDoctorDTO", new RegisterDoctorDTO());
        return "auth/register-doctor";
    }

    // ── POST /register/doctor ─────────────────────────────────────────────────
    @PostMapping("/register/doctor")
    public String registerDoctor(
            @Valid @ModelAttribute RegisterDoctorDTO dto,
            BindingResult result,
            @RequestParam String confirmPassword,
            @RequestParam("docFile") MultipartFile docFile,
            Model model) {

        if (!dto.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "auth/register-doctor";
        }

        if (result.hasErrors()) {
            return "auth/register-doctor";
        }

        try {
            // Aquí guardarías el archivo en disco o en un servicio de almacenamiento
            // y obtendrías la URL para guardarlo en docUrl
            String docUrl = "/uploads/" + docFile.getOriginalFilename();

            Doctor doctor = new Doctor();
            doctor.setEmail(dto.getEmail());
            doctor.setPassword(dto.getPassword());
            doctor.setNombre(dto.getNombre());
            doctor.setEspecialidad(dto.getEspecialidad());
            doctor.setLicenciaMedica(dto.getLicenciaMedica());
            doctor.setDocUrl(docUrl);

            authService.registerDoctor(doctor);

            return "redirect:/login?registered";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register-doctor";
        }
    }
}
