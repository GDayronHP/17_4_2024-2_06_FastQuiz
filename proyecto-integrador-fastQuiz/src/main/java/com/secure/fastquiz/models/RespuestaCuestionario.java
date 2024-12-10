package com.secure.fastquiz.models;

import jakarta.persistence.*;

@Entity
public class RespuestaCuestionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuestionario_id")
    private Cuestionario cuestionario;

    @ManyToOne
    @JoinColumn(name = "pregunta_seleccionada_id")
    private PreguntaSeleccionada preguntaSeleccionada;

    private String claveAlternativaSeleccionada;

    // Cambio de tipo de String a Long para idRealPregunta
    private Long idRealPregunta;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public PreguntaSeleccionada getPreguntaSeleccionada() {
        return preguntaSeleccionada;
    }

    public void setPreguntaSeleccionada(PreguntaSeleccionada preguntaSeleccionada) {
        this.preguntaSeleccionada = preguntaSeleccionada;
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
