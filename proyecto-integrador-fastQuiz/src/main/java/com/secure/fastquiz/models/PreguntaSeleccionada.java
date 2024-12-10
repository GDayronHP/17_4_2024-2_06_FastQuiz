package com.secure.fastquiz.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_seleccionadas")
public class PreguntaSeleccionada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuestionario_id")
    @JsonBackReference
    private Cuestionario cuestionario;

    @ManyToOne
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;

    @Column(name = "texto_pregunta_seleccionada", columnDefinition = "TEXT")
    private String textoPreguntaSeleccionada;

    @Column(name = "alternativas", columnDefinition = "TEXT")
    private String alternativasJson;  // Almacenar alternativas en formato JSON

    // Nuevos campos: tiempo y requerido
    @Column(name = "tiempo")
    private Integer tiempo; // Tiempo en segundos

    @Column(name = "requerido")
    private Boolean requerido; // Indica si la pregunta es obligatoria

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public String getTextoPreguntaSeleccionada() {
        return textoPreguntaSeleccionada;
    }

    public void setTextoPreguntaSeleccionada(String textoPreguntaSeleccionada) {
        this.textoPreguntaSeleccionada = textoPreguntaSeleccionada;
    }

    public String getAlternativasJson() {
        return alternativasJson;
    }

    public void setAlternativasJson(String alternativasJson) {
        this.alternativasJson = alternativasJson;
    }

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
