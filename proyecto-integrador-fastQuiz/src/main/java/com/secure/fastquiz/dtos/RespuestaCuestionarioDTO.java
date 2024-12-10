package com.secure.fastquiz.dtos;

import java.util.List;

public class RespuestaCuestionarioDTO {

    private Long cuestionarioId;
    private List<PreguntaRespuestaDTO> preguntasRespuestas;

    // Getters y Setters
    public Long getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(Long cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
    }

    public List<PreguntaRespuestaDTO> getPreguntasRespuestas() {
        return preguntasRespuestas;
    }

    public void setPreguntasRespuestas(List<PreguntaRespuestaDTO> preguntasRespuestas) {
        this.preguntasRespuestas = preguntasRespuestas;
    }

    // Clase interna para representar cada pregunta respondida
    public static class PreguntaRespuestaDTO {
        private Long preguntaSeleccionadaId;
        private String claveAlternativaSeleccionada;
        private Long idRealPregunta; // Corregido a Long para reflejar la pregunta real

        // Getters y Setters
        public Long getPreguntaSeleccionadaId() {
            return preguntaSeleccionadaId;
        }

        public void setPreguntaSeleccionadaId(Long preguntaSeleccionadaId) {
            this.preguntaSeleccionadaId = preguntaSeleccionadaId;
        }

        public String getClaveAlternativaSeleccionada() {
            return claveAlternativaSeleccionada;
        }

        public void setClaveAlternativaSeleccionada(String claveAlternativaSeleccionada) {
            this.claveAlternativaSeleccionada = claveAlternativaSeleccionada;
        }

        public Long getIdRealPregunta() {
            return idRealPregunta;
        }

        public void setIdRealPregunta(Long idRealPregunta) {
            this.idRealPregunta = idRealPregunta;
        }
    }
}
