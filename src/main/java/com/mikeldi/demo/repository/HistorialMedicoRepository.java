package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mikeldi.demo.model.HistorialMedico;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Long> {
	
	// Obtener el expediente de un paciente concreto
	Optional<HistorialMedico> findByPaciente_Id(Long pacienteId);

	boolean existsByPaciente_Id(Long pacienteId);

}
