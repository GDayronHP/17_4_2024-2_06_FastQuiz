package com.secure.fastquiz.dtos;

public class ResultadoComparacionDTO {
    private Long idRealPregunta;
    private String claveSeleccionada;
    private String claveCorrecta;
    private boolean esCorrecta;

    // Getters y Setters
    public Long getIdRealPregunta() {
        return idRealPregunta;
    }

    public void setIdRealPregunta(Long idRealPregunta) {
        this.idRealPregunta = idRealPregunta;
    }

    public String getClaveSeleccionada() {
        return claveSeleccionada;
    }

    public void setClaveSeleccionada(String claveSeleccionada) {
        this.claveSeleccionada = claveSeleccionada;
    }

    public String getClaveCorrecta() {
        return claveCorrecta;
    }

    public void setClaveCorrecta(String claveCorrecta) {
        this.claveCorrecta = claveCorrecta;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }
}
