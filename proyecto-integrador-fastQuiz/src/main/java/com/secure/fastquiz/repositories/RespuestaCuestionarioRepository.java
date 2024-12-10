package com.secure.fastquiz.repositories;

import com.secure.fastquiz.models.Cuestionario;
import com.secure.fastquiz.models.RespuestaCuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaCuestionarioRepository extends JpaRepository<RespuestaCuestionario, Long> {
    List<RespuestaCuestionario> findByCuestionario(Cuestionario cuestionario);
}