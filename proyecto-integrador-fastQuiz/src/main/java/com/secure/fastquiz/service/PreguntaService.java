package com.secure.fastquiz.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.fastquiz.models.Alternativa;
import com.secure.fastquiz.models.Pregunta;
import com.secure.fastquiz.models.RespuestaCorrecta;
import com.secure.fastquiz.repositories.AlternativaRepository;
import com.secure.fastquiz.repositories.PreguntaRepository;
import com.secure.fastquiz.repositories.RespuestaCorrectaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;
    @Autowired
    private AlternativaRepository alternativaRepository;
    @Autowired
    private RespuestaCorrectaRepository respuestaCorrectaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper(); // Instancia de ObjectMapper

    // Metodo para obtener todas las preguntas de un registro específico
    public List<String> obtenerTodasLasPreguntas(Long id) {
        Optional<Pregunta> optionalPregunta = preguntaRepository.findById(id);

        if (optionalPregunta.isPresent()) {
            Pregunta pregunta = optionalPregunta.get();
            String preguntasJson = pregunta.getPreguntasJson();

            try {
                // Deserializar JSON a un Map de preguntas
                Map<String, String> preguntasMap = objectMapper.readValue(preguntasJson, new TypeReference<Map<String, String>>() {});
                return new ArrayList<>(preguntasMap.values()); // Devolver solo los valores (preguntas)
            } catch (Exception e) {
                throw new RuntimeException("Error al deserializar las preguntas JSON", e);
            }
        }
        return new ArrayList<>(); // Devuelve una lista vacía si no se encuentra el ID
    }

    // Metodo para buscar alternativas de una pregunta específica por ID y número de pregunta
    public Map<String, String> obtenerAlternativasDePregunta(Long id, int numeroPregunta) {
        Optional<Pregunta> optionalPregunta = preguntaRepository.findById(id);

        if (optionalPregunta.isPresent()) {
            Pregunta pregunta = optionalPregunta.get();
            List<Alternativa> alternativas = alternativaRepository.findAllByPregunta(pregunta);

            if (!alternativas.isEmpty()) {
                Alternativa alternativa = alternativas.get(0); // Solo obtenemos el primer registro de alternativas JSON
                String alternativasJson = alternativa.getAlternativasJson();

                try {
                    // Deserializar el JSON a una lista de mapas de alternativas
                    List<Map<String, String>> alternativasList = objectMapper.readValue(alternativasJson, new TypeReference<List<Map<String, String>>>() {});

                    // Asegurarnos de que el índice de la pregunta sea válido
                    if (numeroPregunta - 1 < alternativasList.size()) {
                        // Obtener el mapa de alternativas de la pregunta
                        return alternativasList.get(numeroPregunta - 1);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error al deserializar las alternativas JSON", e);
                }
            }
        }
        return new HashMap<>(); // Devuelve un mapa vacío si no se encuentra la pregunta o las alternativas
    }


    // Metodo para obtener la respuesta correcta de una pregunta específica por ID y número de pregunta
    public String obtenerRespuestaCorrectaDePregunta(Long id, int numeroPregunta) {
        Optional<Pregunta> optionalPregunta = preguntaRepository.findById(id);

        if (optionalPregunta.isPresent()) {
            Pregunta pregunta = optionalPregunta.get();
            RespuestaCorrecta respuestaCorrecta = respuestaCorrectaRepository.findByPregunta(pregunta);

            if (respuestaCorrecta != null) {
                String respuestasCorrectasJson = respuestaCorrecta.getRespuestasCorrectasJson();
                try {
                    // Deserializar JSON a un Map de respuestas correctas
                    Map<String, String> respuestasCorrectasMap = objectMapper.readValue(respuestasCorrectasJson, new TypeReference<Map<String, String>>() {});
                    return respuestasCorrectasMap.get("Respuesta " + numeroPregunta); // Devolver la respuesta correcta específica
                } catch (Exception e) {
                    throw new RuntimeException("Error al deserializar las respuestas correctas JSON", e);
                }
            }
        }
        return null; // Devuelve null si no se encuentra el ID o la pregunta
    }
    // Método para obtener las respuestas correctas de una pregunta específica por ID
    public Map<String, String> obtenerRespuestasCorrectasDePregunta(Long id) {
        Optional<Pregunta> optionalPregunta = preguntaRepository.findById(id);

        if (optionalPregunta.isPresent()) {
            Pregunta pregunta = optionalPregunta.get();
            RespuestaCorrecta respuestaCorrecta = respuestaCorrectaRepository.findByPregunta(pregunta);

            if (respuestaCorrecta != null) {
                try {
                    // Deserializar JSON de respuestas correctas
                    Map<String, String> respuestasCorrectas = objectMapper.readValue(respuestaCorrecta.getRespuestasCorrectasJson(), new TypeReference<Map<String, String>>() {});
                    return respuestasCorrectas;
                } catch (Exception e) {
                    throw new RuntimeException("Error al deserializar las respuestas correctas", e);
                }
            }
        }
        return null; // Devuelve null si no se encuentra la pregunta o las respuestas correctas
    }



}
