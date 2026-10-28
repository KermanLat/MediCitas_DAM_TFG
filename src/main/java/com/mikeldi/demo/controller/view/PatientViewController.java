package com.mikeldi.demo.controller.view;

import com.mikeldi.demo.model.PeticionCita;
import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.model.HistorialMedico;
import com.mikeldi.demo.model.Paciente;
import com.mikeldi.demo.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientViewController {

    private final PacienteService            patientService;
    private final CitaService        		 appointmentService;
    private final PeticionCitaService 		requestService;
    private final NotificacionService       notificationService;
    private final DoctorService             doctorService;
    private final HistorialMedicoService      medicalRecordService;
    
    

    public PatientViewController(PacienteService patientService, CitaService appointmentService,
			PeticionCitaService requestService, NotificacionService notificationService, DoctorService doctorService,
			HistorialMedicoService medicalRecordService) {
		super();
		this.patientService = patientService;
		this.appointmentService = appointmentService;
		this.requestService = requestService;
		this.notificationService = notificationService;
		this.doctorService = doctorService;
		this.medicalRecordService = medicalRecordService;
	}

	// ── Helper: atributos comunes del sidebar ─────────────────────────────────
    private Paciente addSidebarAttrs(UserDetails userDetails, Model model) {
        Paciente patient = patientService.findByEmail(userDetails.getUsername());
        model.addAttribute("patient",              patient);
        model.addAttribute("unseenNotifications",  notificationService.countUnseen(patient.getId()));
        model.addAttribute("pendingCount",         requestService.findPendingByPatient(patient.getId()).size());
        model.addAttribute("newAppointments",      notificationService.countNewAppointments(patient.getId()));
        return patient;
    }

    // ── GET /patient/dashboard ────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Paciente patient = addSidebarAttrs(userDetails, model);

        var todayList = appointmentService.findTodayByPatient(patient.getId());
        var upcoming  = appointmentService.findUpcomingByPatient(patient.getId());
        var pending   = requestService.findPendingByPatient(patient.getId());

        model.addAttribute("todayAppointment",     todayList.isEmpty() ? null : todayList.get(0));
        model.addAttribute("upcomingAppointments", upcoming);
        model.addAttribute("pendingRequests",      pending);

        return "paciente/dashboard";
    }

    // ── GET /patient/calendar ─────────────────────────────────────────────────
    @GetMapping("/calendar")
    public String calendar(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        addSidebarAttrs(userDetails, model);
        model.addAttribute("doctors", doctorService.findAllVerified());
        return "paciente/calendario";
    }

    // ── POST /patient/requests/new ────────────────────────────────────────────
    @PostMapping("/requests/new")
    public String createRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam String preferredDate,
            @RequestParam(required = false) String preferredTime,
            @RequestParam(required = false) List<String> altDates,
            @RequestParam String reason) {

        Paciente patient = patientService.findById(patientId);
        Doctor  doctor  = doctorService.findById(doctorId);

        PeticionCita req = new PeticionCita();
        req.setPaciente(patient);
        req.setDoctor(doctor);
        req.setRazon(reason);
        req.setFechaPreferida(LocalDateTime.parse(preferredDate));

        if (altDates != null) {
            req.setFechaAlterna(altDates.stream()
                .filter(d -> d != null && !d.isBlank())
                .map(java.time.LocalDate::parse)
                .toList());
        }

        requestService.create(req);
        return "redirect:/patient/calendar?sent";
    }

    // ── GET /patient/appointments ─────────────────────────────────────────────
    @GetMapping("/appointments")
    public String appointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Paciente patient = addSidebarAttrs(userDetails, model);
        model.addAttribute("appointments", appointmentService.findUpcomingByPatient(patient.getId()));
        return "paciente/citas";
    }

    // ── POST /patient/appointments/{id}/cancel ────────────────────────────────
    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.cancel(id);
        return "redirect:/patient/appointments";
    }

    // ── POST /patient/appointments/{id}/reschedule ────────────────────────────
    @PostMapping("/appointments/{id}/reschedule")
    public String rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam String scheduledAt) {
        appointmentService.reschedule(id, LocalDateTime.parse(scheduledAt));
        return "redirect:/patient/appointments";
    }

    // ── GET /patient/requests ─────────────────────────────────────────────────
    @GetMapping("/requests")
    public String requests(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Paciente patient = addSidebarAttrs(userDetails, model);
        model.addAttribute("requests", requestService.findByPatient(patient.getId()));
        return "paciente/solicitudes";
    }

    // ── POST /patient/requests/{id}/cancel ────────────────────────────────────
    @PostMapping("/requests/{id}/cancel")
    public String cancelRequest(@PathVariable Long id) {
        requestService.reject(id);
        return "redirect:/patient/requests";
    }

 // ── GET /patient/notifications ────────────────────────────────────────────
    @GetMapping("/notifications")
    public String notifications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Paciente patient = addSidebarAttrs(userDetails, model);
        model.addAttribute("notifications", notificationService.findByUser(patient.getId()));
        return "paciente/notificaciones";
    }

    // ── POST /patient/notifications/seen-all ──────────────────────────────────
    @PostMapping("/notifications/seen-all")
    public String markAllNotificationsSeen(@AuthenticationPrincipal UserDetails userDetails) {
        Paciente patient = patientService.findByEmail(userDetails.getUsername());
        notificationService.markAllAsSeen(patient.getId());
        return "redirect:/patient/notifications";
    }

    // ── GET /patient/profile ──────────────────────────────────────────────────
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Paciente patient = addSidebarAttrs(userDetails, model);
        HistorialMedico record = null;
        try { record = medicalRecordService.findByPatient(patient.getId()); }
        catch (Exception ignored) {}
        model.addAttribute("record", record);
        return "paciente/perfil";
    }

    // ── POST /patient/profile ─────────────────────────────────────────────────
    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute Paciente updated) {
        Paciente patient = patientService.findByEmail(userDetails.getUsername());
        patientService.updateProfile(patient.getId(), updated);
        return "redirect:/patient/profile?saved";
    }
}
