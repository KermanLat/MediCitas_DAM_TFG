package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mikeldi.demo.model.PeticionCita;
import com.mikeldi.demo.model.PeticionCita.ReqStatus;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PeticionCitaRepository extends JpaRepository<PeticionCita, Long> {
	
	// Solicitudes de un paciente concreto
    List<PeticionCita> findByPaciente_Id(Long patientId);
 
    // Solicitudes dirigidas a un doctor concreto
    List<PeticionCita> findByDoctor_Id(Long doctorId);
 
    // Solicitudes pendientes de un doctor (las que ve en el dashboard)
    List<PeticionCita> findByDoctor_IdAndEstado(Long doctorId, ReqStatus status);
 
    // Solicitudes pendientes de un paciente (las que está esperando que acepten)
    List<PeticionCita> findByPaciente_IdAndEstado(Long patientId, ReqStatus status);
}
