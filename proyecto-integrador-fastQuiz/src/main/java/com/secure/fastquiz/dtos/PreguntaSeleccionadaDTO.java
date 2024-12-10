package com.secure.fastquiz.dtos;

import java.util.List;

public class PreguntaSeleccionadaDTO {
    private Long id;
    private String textoPreguntaSeleccionada;
    private List<AlternativaDTO> alternativas;

    // Nuevos campos: tiempo y requerido
    private Integer tiempo;
    private Boolean requerido;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextoPreguntaSeleccionada() {
        return textoPreguntaSeleccionada;
    }

    public void setTextoPreguntaSeleccionada(String textoPreguntaSeleccionada) {
        this.textoPreguntaSeleccionada = textoPreguntaSeleccionada;
    }

    public List<AlternativaDTO> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<AlternativaDTO> alternativas) {
        this.alternativas = alternativas;
    }

    // Getters y setters para los nuevos campos
    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public Boolean getRequerido() {
        return requerido;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }
}
