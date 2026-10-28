package com.mikeldi.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "mensajes")
public class Mensaje {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    // Doctor que envía el mensaje
    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private Doctor sender;
 
    // Doctor que recibe el mensaje
    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Doctor receiver;
 
    @Column(nullable = false, length = 5000)
    private String contenido;
 
    @Column(name = "fecha_envio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;
 
    @Column(nullable = false)
    private Boolean leido = false;
 
    public Mensaje() {
        super();
    }
 
    @PrePersist
    protected void onCreate() {
        this.fechaEnvio = LocalDateTime.now();
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Doctor getSender() {
        return sender;
    }
 
    public void setSender(Doctor sender) {
        this.sender = sender;
    }
 
    public Doctor getReceiver() {
        return receiver;
    }
 
    public void setReceiver(Doctor receiver) {
        this.receiver = receiver;
    }
 
    public String getContenido() {
        return contenido;
    }
 
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
 
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
 
    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
 
    public Boolean getLeido() {
        return leido;
    }
 
    public void setLeido(Boolean leido) {
        this.leido = leido;
    }
}
