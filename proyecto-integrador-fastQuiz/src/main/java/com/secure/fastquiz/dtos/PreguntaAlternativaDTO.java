package com.secure.fastquiz.dtos;

import java.util.List;

public class PreguntaAlternativaDTO {
    private String pregunta;
    private List<String> alternativas;
    private String respuestaCorrecta; // Esto se mantiene, pero no lo enviamos al cliente si no lo deseas

    public PreguntaAlternativaDTO(String pregunta, List<String> alternativas, String respuestaCorrecta) {
        this.pregunta = pregunta;
        this.alternativas = alternativas;
        this.respuestaCorrecta = respuestaCorrecta; // Se mantiene, pero no la devolveremos si no es necesario
    }

    // Getters y setters
    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<String> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<String> alternativas) {
        this.alternativas = alternativas;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }
}
