package com.secure.fastquiz.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "b_correctas")
public class RespuestaCorrecta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String respuestasCorrectasJson;

    @OneToOne
    @JoinColumn(name = "pregunta_id")
    @JsonBackReference
    private Pregunta pregunta;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    // Constructor, getters y setters

    public RespuestaCorrecta() {}

    public RespuestaCorrecta(String respuestasCorrectasJson, Pregunta pregunta) {
        this.respuestasCorrectasJson = respuestasCorrectasJson;
        this.pregunta = pregunta;
    }

    public Long getId() {
        return id;
    }

    public String getRespuestasCorrectasJson() {
        return respuestasCorrectasJson;
    }

    public void setRespuestasCorrectasJson(String respuestasCorrectasJson) {
        this.respuestasCorrectasJson = respuestasCorrectasJson;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
