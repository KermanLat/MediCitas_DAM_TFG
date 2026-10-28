package com.mikeldi.demo.controller.view;

import com.mikeldi.demo.dto.PacienteSummaryDTO;
import com.mikeldi.demo.model.Cita;
import com.mikeldi.demo.model.Doctor;
import com.mikeldi.demo.model.HistorialMedico;
import com.mikeldi.demo.model.Paciente;
import com.mikeldi.demo.model.SesionNota;
import com.mikeldi.demo.service.CitaService;
import com.mikeldi.demo.service.DoctorService;
import com.mikeldi.demo.service.HistorialMedicoService;
import com.mikeldi.demo.service.MensajeService;
import com.mikeldi.demo.service.NotificacionService;
import com.mikeldi.demo.service.PacienteService;
import com.mikeldi.demo.service.PeticionCitaService;
import com.mikeldi.demo.service.SesionNotaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor")
public class DoctorViewController {

    private final DoctorService doctorService;
    private final CitaService citaService;
    private final PeticionCitaService peticionCitaService;
    private final MensajeService mensajeService;
    private final NotificacionService notificacionService;
    private final PacienteService pacienteService;
    private final HistorialMedicoService historialMedicoService;
    private final SesionNotaService sesionNotaService;

    public DoctorViewController(DoctorService doctorService,
                                 CitaService citaService,
                                 PeticionCitaService peticionCitaService,
                                 MensajeService mensajeService,
                                 NotificacionService notificacionService,
                                 PacienteService pacienteService,
                                 HistorialMedicoService historialMedicoService,
                                 SesionNotaService sesionNotaService) {
        this.doctorService = doctorService;
        this.citaService = citaService;
        this.peticionCitaService = peticionCitaService;
        this.mensajeService = mensajeService;
        this.notificacionService = notificacionService;
        this.pacienteService = pacienteService;
        this.historialMedicoService = historialMedicoService;
        this.sesionNotaService = sesionNotaService;
    }

    // ── Helper: atributos comunes del sidebar ─────────────────────────────────
    private Doctor addSidebarAttrs(UserDetails userDetails, Model model) {
        Doctor doctor = doctorService.findByEmail(userDetails.getUsername());
        model.addAttribute("doctor",              doctor);
        model.addAttribute("unreadMessages",      mensajeService.countUnread(doctor.getId()));
        model.addAttribute("unseenNotifications", notificacionService.countUnseen(doctor.getId()));
        return doctor;
    }

    // ── GET /doctor/dashboard ─────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Doctor doctor = addSidebarAttrs(userDetails, model);
        model.addAttribute("todayAppointments", citaService.findTodayByDoctor(doctor.getId()));
        model.addAttribute("pendingRequests",   peticionCitaService.findPendingByDoctor(doctor.getId()));
        model.addAttribute("totalPatients",     pacienteService.findAll().size());
        return "doctor/dashboard";
    }

    // ── GET /doctor/calendar ──────────────────────────────────────────────────
    @GetMapping("/calendar")
    public String calendar(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Doctor doctor = addSidebarAttrs(userDetails, model);
        model.addAttribute("hasGcalToken", doctor.getGcalToken() != null && !doctor.getGcalToken().isBlank());
        return "doctor/calendario";
    }
    @GetMapping("/calendar/events")
    @ResponseBody
    public List<Map<String, Object>> calendarEvents(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String start,
            @RequestParam String end) {

        Doctor doctor = doctorService.findByEmail(userDetails.getUsername());
        start = start.replace(" ", "+"); // Fix: restaurar el + del offset
        LocalDateTime from = java.time.ZonedDateTime.parse(start).toLocalDateTime();
        end = end.replace(" ", "+"); // Fix: restaurar el + del offset
        LocalDateTime to   = java.time.ZonedDateTime.parse(end).toLocalDateTime();

        return citaService.findByDoctorAndRange(doctor.getId(), from, to)
            .stream()
            .map(c -> {
                Map<String, Object> event = new java.util.HashMap<>();
                event.put("id",    c.getId());
                event.put("title", "🩺 " + c.getPaciente().getNombre());
                event.put("start", c.getFechaProgramada().toString());
                event.put("end",   c.getFechaProgramada().plusHours(1).toString());
                event.put("color", switch (c.getEstado()) {
                    case PROGRAMADO  -> "#4a7c59";
                    case CANCELADO   -> "#c0392b";
                    case COMPLETADO  -> "#5a5a5a";
                    default          -> "#4a7c59";
                });
                return event;
            })
            .collect(java.util.stream.Collectors.toList());
    }
    
    // ── GET /doctor/appointment/{id} ──────────────────────────────────────────────────
    
    @GetMapping("/appointment/{id}")
    public String appointmentDetail(@PathVariable Long id, 
    		@AuthenticationPrincipal UserDetails userDetails,
    		Model model) {
        Cita appt = citaService.findById(id);
        Doctor doctor = addSidebarAttrs(userDetails, model);
        model.addAttribute("cita", appt);
        model.addAttribute("record", appt.getPaciente().getHistorialMedico());
        model.addAttribute("notes", sesionNotaService.findByAppointment(id));
        return "doctor/appointment";
    }

    @PostMapping("/appointment/{id}/complete")
    public String completeAppointment(@PathVariable Long id) {
        citaService.complete(id);
        return "redirect:/doctor/appointment/" + id;
    }
    
    // ── POST /doctor/appointment/{id}/notes ───────────────────────────────────
    @PostMapping("/appointment/{id}/notes")
    public String addNoteToAppointment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestParam String contenido) {
        Doctor doctor = doctorService.findByEmail(userDetails.getUsername());
        Cita cita = citaService.findById(id);

        SesionNota nota = new SesionNota();
        nota.setDoctor(doctor);
        nota.setCita(cita);
        nota.setHistorialMedico(cita.getPaciente().getHistorialMedico());
        nota.setContenido(contenido);
        sesionNotaService.create(nota);

        return "redirect:/doctor/appointment/" + id;
    }

    // ── GET /doctor/patients ──────────────────────────────────────────────────
    @GetMapping("/patients")
    public String patients(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String search,
            Model model) {
        Doctor doctor = addSidebarAttrs(userDetails, model);

        List<Paciente> pacientes = (search != null && !search.isBlank())
                ? pacienteService.searchByName(search)
                : pacienteService.findAll();

        List<PacienteSummaryDTO> summaries = pacientes.stream()
                .map(p -> {
                    String enfermedad = null;
                    try {
                        enfermedad = historialMedicoService
                                .findByPatient(p.getId())
                                .getEnfermedadPrincipal();
                    } catch (Exception ignored) {}
                    return PacienteSummaryDTO.from(p, enfermedad);
                })
                .collect(Collectors.toList());

        model.addAttribute("patients", summaries);
        model.addAttribute("search",   search);
        return "doctor/pacientes";
    }

    // ── GET /doctor/patients/{id} ─────────────────────────────────────────────
    @GetMapping("/patients/{id}")
    public String expediente(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            Model model) {
        addSidebarAttrs(userDetails, model);
        Paciente paciente = pacienteService.findById(id);
        HistorialMedico historial;
        try {
            historial = historialMedicoService.findByPatient(id);
        } catch (Exception e) {
            historial = new HistorialMedico();
        }

        // Todas las notas del paciente (de todas sus citas)
        List<SesionNota> notas = sesionNotaService.findByPaciente(id);

        model.addAttribute("patient", paciente);
        model.addAttribute("record", historial);
        model.addAttribute("notes", notas);
        return "doctor/expediente";
    }

    // ── POST /doctor/patients/{id}/record ─────────────────────────────────────
    @PostMapping("/patients/{id}/record")
    public String updateRecord(
            @PathVariable Long id,
            @ModelAttribute HistorialMedico updated) {
        historialMedicoService.update(id, updated);
        return "redirect:/doctor/patients/" + id;
    }

    // ── POST /doctor/patients/{id}/notes ──────────────────────────────────────
    @PostMapping("/patients/{id}/notes")
    public String addNote(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestParam String contenido) {
        Doctor doctor = doctorService.findByEmail(userDetails.getUsername());
        HistorialMedico historial = historialMedicoService.findByPatient(id);

        SesionNota nota = new SesionNota();
        nota.setDoctor(doctor);
        nota.setHistorialMedico(historial);
        nota.setContenido(contenido);
        sesionNotaService.create(nota);

        return "redirect:/doctor/patients/" + id;
    }

    // ── POST /doctor/appointment/{citaId}/notes/{noteId}/delete ──────────────
    @PostMapping("/appointment/{citaId}/notes/{noteId}/delete")
    public String deleteNoteFromCita(
            @PathVariable Long citaId,
            @PathVariable Long noteId) {
        sesionNotaService.delete(noteId);
        return "redirect:/doctor/appointment/" + citaId;
    }
 
    // ── POST /doctor/patients/{patientId}/notes/{noteId}/delete ──────────────
    @PostMapping("/patients/{patientId}/notes/{noteId}/delete")
    public String deleteNoteFromExpediente(
            @PathVariable Long patientId,
            @PathVariable Long noteId) {
        sesionNotaService.delete(noteId);
        return "redirect:/doctor/patients/" + patientId;
    }

    // ── POST /doctor/patients/{id}/discharge ──────────────────────────────────
    @PostMapping("/patients/{id}/discharge")
    public String discharge(@PathVariable Long id) {
        pacienteService.discharge(id);
        return "redirect:/doctor/patients";
    }

    // ── GET /doctor/messages ──────────────────────────────────────────────────
    @GetMapping("/messages")
    public String messages(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Doctor doctor = addSidebarAttrs(userDetails, model);
        model.addAttribute("conversations", mensajeService.findConversations(doctor.getId()));
        model.addAttribute("activeConv",    null);
        model.addAttribute("messages",      List.of());
        model.addAttribute("activeConvId",  null);
        model.addAttribute("allDoctors",    doctorService.findAllVerified()
                .stream()
                .filter(d -> !d.getId().equals(doctor.getId())) // excluir al doctor actual
                .collect(Collectors.toList()));
        return "doctor/mensajes";
    }

    // ── GET /doctor/messages/{receiverId} ─────────────────────────────────────
    @GetMapping("/messages/{receiverId}")
    public String conversation(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long receiverId,
            Model model) {
        Doctor doctor = addSidebarAttrs(userDetails, model);
        Doctor receiver = doctorService.findById(receiverId);

        model.addAttribute("conversations", mensajeService.findConversations(doctor.getId()));
        model.addAttribute("activeConv",    receiver);
        model.addAttribute("messages",      mensajeService.findConversation(doctor.getId(), receiverId));
        model.addAttribute("activeConvId",  receiverId);

        mensajeService.markConversationAsRead(doctor.getId(), receiverId);
        return "doctor/mensajes";
    }

    // ── POST /doctor/messages/send ────────────────────────────────────────────
    @PostMapping("/messages/send")
    public String sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long receiverId,
            @RequestParam String contenido) {
        Doctor doctor = doctorService.findByEmail(userDetails.getUsername());
        mensajeService.send(doctor.getId(), receiverId, contenido);
        return "redirect:/doctor/messages/" + receiverId;
    }
    
 // ── GET /doctor/notifications ─────────────────────────────────────────────
    @GetMapping("/notifications")
    public String notifications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Doctor doctor = addSidebarAttrs(userDetails, model);
        model.addAttribute("notifications", notificacionService.findByUser(doctor.getId()));
        return "doctor/notificaciones";
    }
 
    // ── POST /doctor/notifications/seen-all ───────────────────────────────────
    @PostMapping("/notifications/seen-all")
    public String markAllSeen(@AuthenticationPrincipal UserDetails userDetails) {
        Doctor doctor = doctorService.findByEmail(userDetails.getUsername());
        notificacionService.markAllAsSeen(doctor.getId());
        return "redirect:/doctor/notifications";
    }

    // ── GET /doctor/profile ───────────────────────────────────────────────────
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        addSidebarAttrs(userDetails, model);
        return "doctor/perfil";
    }

    // ── POST /doctor/profile ──────────────────────────────────────────────────
    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute Doctor updated) {
        Doctor doctor = doctorService.findByEmail(userDetails.getUsername());
        doctorService.updateProfile(doctor.getId(), updated);
        return "redirect:/doctor/profile?saved";
    }

    // ── POST /doctor/requests/{id}/accept ─────────────────────────────────────
    @PostMapping("/requests/{id}/accept")
    public String acceptRequest(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable Long id,
                                @RequestParam String fechaSeleccionada,
                                @RequestParam String sala) {
        try {
            LocalDateTime fecha = LocalDateTime.parse(fechaSeleccionada);
            peticionCitaService.accept(id, fecha, sala);
        } catch (Exception e) {
            System.out.println("ERROR al aceptar: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/doctor/dashboard";
    }

    // ── POST /doctor/requests/{id}/reject ─────────────────────────────────────
    @PostMapping("/requests/{id}/reject")
    public String rejectRequest(@PathVariable Long id) {
        peticionCitaService.reject(id);
        return "redirect:/doctor/dashboard";
    }
}
