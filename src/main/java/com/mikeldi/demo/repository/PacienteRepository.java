package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mikeldi.demo.model.Paciente;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	
	Optional<Paciente> findByEmail(String email);

    List<Paciente> findByNameContainingIgnoreCase(String name);

    boolean existsByEmail(String email);

}
