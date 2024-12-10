package com.secure.fastquiz.controllers;

import com.secure.fastquiz.models.Pregunta;
import com.secure.fastquiz.service.ChatGptService;
import com.secure.fastquiz.service.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gpt")
public class ChatGptController {

    @Autowired
    private ChatGptService chatGptService;

    @Autowired
    private PreguntaService preguntaService;

    @PostMapping("/prompt")
    public ResponseEntity<List<Pregunta>> promptOpenAi(@RequestBody Map<String, String> request,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        String prompt = request.get("prompt");
        List<Pregunta> preguntas = chatGptService.sendPrompt(prompt);
        return new ResponseEntity<>(preguntas, HttpStatus.OK);
    }

    @GetMapping("/preguntas/{id}")
    public ResponseEntity<List<String>> obtenerTodasLasPreguntas(@PathVariable Long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        List<String> preguntas = preguntaService.obtenerTodasLasPreguntas(id);
        if (preguntas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(preguntas, HttpStatus.OK);
    }

    @GetMapping("/alternativas/{id}/{numeroPregunta}")
    public ResponseEntity<Map<String, String>> obtenerAlternativasDePregunta(
            @PathVariable Long id,
            @PathVariable int numeroPregunta,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        Map<String, String> alternativas = preguntaService.obtenerAlternativasDePregunta(id, numeroPregunta);
        if (alternativas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(alternativas, HttpStatus.OK);
    }

    @GetMapping("/respuesta/{id}/{numeroPregunta}")
    public ResponseEntity<String> obtenerRespuestaCorrectaDePregunta(
            @PathVariable Long id,
            @PathVariable int numeroPregunta,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        String respuestaCorrecta = preguntaService.obtenerRespuestaCorrectaDePregunta(id, numeroPregunta);
        if (respuestaCorrecta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(respuestaCorrecta, HttpStatus.OK);
    }

    @GetMapping("/respuestas-correctas/{id}")
    public ResponseEntity<Map<String, String>> obtenerRespuestasCorrectas(@PathVariable Long id,
                                                                          @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        Map<String, String> respuestasCorrectas = preguntaService.obtenerRespuestasCorrectasDePregunta(id);
        return new ResponseEntity<>(respuestasCorrectas, HttpStatus.OK);
    }
}
