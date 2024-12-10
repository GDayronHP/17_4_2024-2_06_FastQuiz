package com.secure.fastquiz.dtos;

public class ResultadoRespuestaDTO {
    private Long idRealPregunta;
    private boolean esCorrecta;
    private String claveAlternativaSeleccionada;

    public ResultadoRespuestaDTO(Long idRealPregunta, boolean esCorrecta, String claveAlternativaSeleccionada) {
        this.idRealPregunta = idRealPregunta;
        this.esCorrecta = esCorrecta;
        this.claveAlternativaSeleccionada = claveAlternativaSeleccionada;
    }

    // Getters y setters
    public Long getIdRealPregunta() {
        return idRealPregunta;
    }

    public void setIdRealPregunta(Long idRealPregunta) {
        this.idRealPregunta = idRealPregunta;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }

    public String getClaveAlternativaSeleccionada() {
        return claveAlternativaSeleccionada;
    }

    public void setClaveAlternativaSeleccionada(String claveAlternativaSeleccionada) {
        this.claveAlternativaSeleccionada = claveAlternativaSeleccionada;
    }
}
