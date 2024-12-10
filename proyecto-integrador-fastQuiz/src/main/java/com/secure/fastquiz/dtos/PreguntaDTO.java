package com.secure.fastquiz.dtos;

import java.util.Map;

public class PreguntaDTO {
    private String pregunta;
    private Map<String, String> alternativas;

    public PreguntaDTO(String pregunta, Map<String, String> alternativas) {
        this.pregunta = pregunta;
        this.alternativas = alternativas;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public Map<String, String> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(Map<String, String> alternativas) {
        this.alternativas = alternativas;
    }
}
