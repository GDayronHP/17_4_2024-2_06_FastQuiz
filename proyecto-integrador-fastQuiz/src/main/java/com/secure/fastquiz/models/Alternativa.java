package com.secure.fastquiz.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "b_alternativas")
public class Alternativa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String alternativasJson;

    @ManyToOne
    @JoinColumn(name = "pregunta_id")
    @JsonBackReference
    private Pregunta pregunta;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    // Constructor, getters y setters

    public Alternativa() {}

    public Alternativa(String alternativasJson, Pregunta pregunta) {
        this.alternativasJson = alternativasJson;
        this.pregunta = pregunta;
    }

    public Long getId() {
        return id;
    }

    public String getAlternativasJson() {
        return alternativasJson;
    }

    public void setAlternativasJson(String alternativasJson) {
        this.alternativasJson = alternativasJson;
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
