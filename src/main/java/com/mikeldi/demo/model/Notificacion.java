package com.mikeldi.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "notificaciones")
public class Notificacion {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    // Usuario destinatario (1:N)
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
 
    // Cita que origina la notificación (1:N)
    @ManyToOne(optional = true)
    @JoinColumn(name = "cita_id", nullable = true)
    private Cita cita;
 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotifType tipo;
 
    @Column(nullable = false)
    private Boolean visto = false;
 
    @Column(name = "fecha_creado", nullable = false, updatable = false)
    private LocalDateTime fechaCreado;
 
    public Notificacion() {
        super();
    }
 
    @PrePersist
    protected void onCreate() {
        this.fechaCreado = LocalDateTime.now();
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Usuario getUsuario() {
        return usuario;
    }
 
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
 
    public Cita getCita() {
        return cita;
    }
 
    public void setCita(Cita cita) {
        this.cita = cita;
    }
 
    public NotifType getTipo() {
        return tipo;
    }
 
    public void setTipo(NotifType tipo) {
        this.tipo = tipo;
    }
 
    public Boolean getVisto() {
        return visto;
    }
 
    public void setVisto(Boolean visto) {
        this.visto = visto;
    }
 
    public LocalDateTime getFechaCreado() {
        return fechaCreado;
    }
 
    public void setFechaCreado(LocalDateTime fechaCreado) {
        this.fechaCreado = fechaCreado;
    }
 
    public enum NotifType {
        ACEPTADO,            // solicitud aceptada → paciente
        RECHAZADA,           // solicitud rechazada → paciente
        RECORDATORIO,        // recordatorio de cita → paciente
        SOLICITUD_PENDIENTE  // nueva solicitud pendiente → doctor
    }
}
