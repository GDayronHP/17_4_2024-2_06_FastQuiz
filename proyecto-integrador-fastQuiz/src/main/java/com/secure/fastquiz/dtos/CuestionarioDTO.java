package com.secure.fastquiz.dtos;

import java.util.List;

public class CuestionarioDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private List<PreguntaSeleccionadaDTO> preguntasSeleccionadas;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<PreguntaSeleccionadaDTO> getPreguntasSeleccionadas() {
        return preguntasSeleccionadas;
    }

    public void setPreguntasSeleccionadas(List<PreguntaSeleccionadaDTO> preguntasSeleccionadas) {
        this.preguntasSeleccionadas = preguntasSeleccionadas;
    }
}
