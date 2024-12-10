package com.secure.fastquiz.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "b_preguntas")
public class Pregunta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String preguntasJson;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Alternativa> alternativas = new ArrayList<>();

    @OneToOne(mappedBy = "pregunta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private RespuestaCorrecta respuestaCorrecta;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    // Constructor, getters y setters

    public Pregunta() {}

    public Pregunta(String preguntasJson) {
        this.preguntasJson = preguntasJson;
    }

    public Long getId() {
        return id;
    }

    public String getPreguntasJson() {
        return preguntasJson;
    }

    public void setPreguntasJson(String preguntasJson) {
        this.preguntasJson = preguntasJson;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public RespuestaCorrecta getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(RespuestaCorrecta respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
