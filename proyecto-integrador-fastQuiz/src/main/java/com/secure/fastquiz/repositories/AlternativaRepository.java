package com.secure.fastquiz.repositories;

import com.secure.fastquiz.models.Alternativa;
import com.secure.fastquiz.models.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlternativaRepository extends JpaRepository<Alternativa, Long> {
    List<Alternativa> findAllByPregunta(Pregunta pregunta);
}
