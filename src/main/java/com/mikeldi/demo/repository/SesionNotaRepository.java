package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mikeldi.demo.model.SesionNota;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SesionNotaRepository extends JpaRepository<SesionNota, Long> {
	
	// Todas las notas de un expediente médico
    List<SesionNota> findByHistorialMedico_IdOrderByFechaCreadaDesc(Long recordId);
 
    // Todas las notas escritas por un doctor
    List<SesionNota> findByDoctor_IdOrderByFechaCreadaDesc(Long doctorId);
 
    // Notas de una cita concreta (puede haber varias)
    List<SesionNota> findByCita_IdOrderByFechaCreadaDesc(Long citaId);
    
    // Todas las notas de un paciente (a través de su historial)
    List<SesionNota> findByHistorialMedico_Paciente_IdOrderByFechaCreadaDesc(Long pacienteId);
}
