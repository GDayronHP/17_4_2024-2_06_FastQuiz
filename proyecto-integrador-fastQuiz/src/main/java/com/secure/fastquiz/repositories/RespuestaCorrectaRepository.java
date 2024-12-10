package com.secure.fastquiz.repositories;

import com.secure.fastquiz.models.Pregunta;
import com.secure.fastquiz.models.RespuestaCorrecta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaCorrectaRepository extends JpaRepository<RespuestaCorrecta, Long> {
    RespuestaCorrecta findByPregunta(Pregunta pregunta);
}
