package com.secure.fastquiz.repositories;

import com.secure.fastquiz.models.PreguntaSeleccionada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaSeleccionadaRepository extends JpaRepository<PreguntaSeleccionada, Long> {
    List<PreguntaSeleccionada> findByCuestionarioId(Long cuestionarioId);
}