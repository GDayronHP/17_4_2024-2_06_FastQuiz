package com.secure.fastquiz.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.fastquiz.dtos.RespuestaCuestionarioDTO;
import com.secure.fastquiz.dtos.ResultadoComparacionDTO;
import com.secure.fastquiz.models.*;
import com.secure.fastquiz.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RespuestaCuestionarioService {

    @Autowired
    private RespuestaCuestionarioRepository respuestaCuestionarioRepository;

    @Autowired
    private CuestionarioRepository cuestionarioRepository;

    @Autowired
    private PreguntaSeleccionadaRepository preguntaSeleccionadaRepository;

    @Autowired
    private RespuestaCorrectaRepository respuestaCorrectaRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Método para registrar las respuestas
    public void registrarRespuestas(RespuestaCuestionarioDTO respuestaCuestionarioDTO) {
        Cuestionario cuestionario = cuestionarioRepository.findById(respuestaCuestionarioDTO.getCuestionarioId())
                .orElseThrow(() -> new EntityNotFoundException("Cuestionario no encontrado"));

        for (RespuestaCuestionarioDTO.PreguntaRespuestaDTO respuestaDTO : respuestaCuestionarioDTO.getPreguntasRespuestas()) {
            PreguntaSeleccionada preguntaSeleccionada = preguntaSeleccionadaRepository.findById(respuestaDTO.getPreguntaSeleccionadaId())
                    .orElseThrow(() -> new EntityNotFoundException("Pregunta seleccionada no encontrada"));

            String textoPreguntaSeleccionada = preguntaSeleccionada.getTextoPreguntaSeleccionada();
            Long numeroPreguntaReal = obtenerNumeroPreguntaReal(textoPreguntaSeleccionada);

            RespuestaCuestionario respuesta = new RespuestaCuestionario();
            respuesta.setCuestionario(cuestionario);
            respuesta.setPreguntaSeleccionada(preguntaSeleccionada);
            respuesta.setClaveAlternativaSeleccionada(respuestaDTO.getClaveAlternativaSeleccionada());
            respuesta.setIdRealPregunta(numeroPreguntaReal);

            respuestaCuestionarioRepository.save(respuesta);
        }
    }

    // Método para extraer el número de la pregunta real del texto y convertirlo a Long
    private Long obtenerNumeroPreguntaReal(String textoPreguntaSeleccionada) {
        String numeroPreguntaRealStr = textoPreguntaSeleccionada.split("\\.")[0].trim();
        try {
            return Long.parseLong(numeroPreguntaRealStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato incorrecto para el número de la pregunta real", e);
        }
    }

    // Método para obtener las respuestas con el idRealPregunta y la clase seleccionada
    public List<RespuestaCuestionarioDTO.PreguntaRespuestaDTO> obtenerRespuestasPorCuestionario(Long cuestionarioId) {
        Cuestionario cuestionario = cuestionarioRepository.findById(cuestionarioId)
                .orElseThrow(() -> new EntityNotFoundException("Cuestionario no encontrado"));

        List<RespuestaCuestionario> respuestas = respuestaCuestionarioRepository.findByCuestionario(cuestionario);

        List<RespuestaCuestionarioDTO.PreguntaRespuestaDTO> respuestasDTO = new ArrayList<>();
        for (RespuestaCuestionario respuesta : respuestas) {
            RespuestaCuestionarioDTO.PreguntaRespuestaDTO dto = new RespuestaCuestionarioDTO.PreguntaRespuestaDTO();
            dto.setPreguntaSeleccionadaId(respuesta.getPreguntaSeleccionada().getId());
            dto.setClaveAlternativaSeleccionada(respuesta.getClaveAlternativaSeleccionada());
            dto.setIdRealPregunta(respuesta.getIdRealPregunta());
            respuestasDTO.add(dto);
        }

        return respuestasDTO;
    }

    // Método para comparar respuestas seleccionadas con respuestas correctas
    public List<ResultadoComparacionDTO> compararRespuestasConCorrectas(Long cuestionarioId) {
        // Obtener respuestas seleccionadas por el usuario
        List<RespuestaCuestionarioDTO.PreguntaRespuestaDTO> respuestasUsuario = obtenerRespuestasPorCuestionario(cuestionarioId);

        if (respuestasUsuario.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron respuestas para el cuestionario");
        }

        // Obtener respuestas correctas desde el JSON
        Long idPreguntaReal = respuestasUsuario.get(1).getIdRealPregunta();
        Map<String, String> respuestasCorrectas = obtenerRespuestasCorrectasDePregunta(idPreguntaReal);

        // Lista para almacenar los resultados de la comparación
        List<ResultadoComparacionDTO> resultados = new ArrayList<>();

        // Comparar respuestas
        for (RespuestaCuestionarioDTO.PreguntaRespuestaDTO respuestaUsuario : respuestasUsuario) {
            String claveSeleccionada = respuestaUsuario.getClaveAlternativaSeleccionada();

            // Obtener la respuesta correcta completa y extraer solo la clave (letra)
            String claveCorrectaCompleta = respuestasCorrectas.get("Respuesta " + respuestaUsuario.getIdRealPregunta());
            String claveCorrecta = claveCorrectaCompleta != null ? claveCorrectaCompleta.split("\\)")[0].trim() : ""; // Escapar el paréntesis de cierre

            // Crear resultado de la comparación
            ResultadoComparacionDTO resultado = new ResultadoComparacionDTO();
            resultado.setIdRealPregunta(respuestaUsuario.getIdRealPregunta());
            resultado.setClaveSeleccionada(claveSeleccionada);
            resultado.setClaveCorrecta(claveCorrecta);
            resultado.setEsCorrecta(claveSeleccionada != null && claveSeleccionada.equals(claveCorrecta));

            resultados.add(resultado);
        }

        return resultados;
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
                    return objectMapper.readValue(respuestaCorrecta.getRespuestasCorrectasJson(), new TypeReference<Map<String, String>>() {});
                } catch (Exception e) {
                    throw new RuntimeException("Error al deserializar las respuestas correctas", e);
                }
            }
        }
        return Collections.emptyMap();
    }
}
