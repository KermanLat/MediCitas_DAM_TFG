package com.mikeldi.demo.dto;

import java.time.LocalDateTime;

public class RescheduleDTO {
	private LocalDateTime fechaProgramada;

    public RescheduleDTO() {
    }

    public RescheduleDTO(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }
}
