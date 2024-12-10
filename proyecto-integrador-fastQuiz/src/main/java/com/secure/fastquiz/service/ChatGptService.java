package com.secure.fastquiz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.fastquiz.config.OpenAiConfig;
import com.secure.fastquiz.models.Alternativa;
import com.secure.fastquiz.models.Pregunta;
import com.secure.fastquiz.models.RespuestaCorrecta;
import com.secure.fastquiz.repositories.AlternativaRepository;
import com.secure.fastquiz.repositories.PreguntaRepository;
import com.secure.fastquiz.repositories.RespuestaCorrectaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ChatGptService {

    private final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    private OpenAiConfig openAiConfig;

    @Autowired
    private PreguntaRepository preguntaRepository;
    @Autowired
    private AlternativaRepository alternativaRepository;
    @Autowired
    private RespuestaCorrectaRepository respuestaCorrectaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper(); // Instancia de ObjectMapper

    public List<Pregunta> sendPrompt(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAiConfig.getApiKey());

        Map<String, Object> requestBodyMap = new LinkedHashMap<>();
        requestBodyMap.put("model", "gpt-3.5-turbo");
        requestBodyMap.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestBodyMap);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir el cuerpo de la solicitud a JSON", e);
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            String responseBody = response.getBody();
            JsonNode responseJson = objectMapper.readTree(responseBody);
            String content = responseJson.get("choices").get(0).get("message").get("content").asText();

            // Crear estructuras para almacenar preguntas, alternativas y respuestas correctas en formato JSON
            Map<String, String> preguntasMap = new LinkedHashMap<>(); // LinkedHashMap para preservar el orden
            List<Map<String, String>> alternativasList = new LinkedList<>(); // Lista de alternativas
            Map<String, String> respuestasCorrectasMap = new LinkedHashMap<>(); // LinkedHashMap para respuestas correctas

            String[] lines = content.split("\n\n");
            int questionCounter = 1;

            for (String block : lines) {
                String[] linesInBlock = block.split("\n");
                if (linesInBlock.length > 0) {
                    String preguntaText = linesInBlock[0].trim();
                    preguntasMap.put("Pregunta " + questionCounter, preguntaText);

                    Map<String, String> alternativas = new HashMap<>();
                    String respuestaCorrecta = null;

                    for (int i = 1; i < linesInBlock.length; i++) {
                        String line = linesInBlock[i].trim();
                        if (line.startsWith("a)")) {
                            alternativas.put("a", line.substring(3).trim()); // Elimina "a)"
                        } else if (line.startsWith("b)")) {
                            alternativas.put("b", line.substring(3).trim()); // Elimina "b)"
                        } else if (line.startsWith("c)")) {
                            alternativas.put("c", line.substring(3).trim()); // Elimina "c)"
                        } else if (line.startsWith("d)")) { // NUEVA ALTERNATIVA
                            alternativas.put("d", line.substring(3).trim()); // Elimina "d)"
                        } else if (line.startsWith("Respuesta correcta:")) {
                            respuestaCorrecta = line.substring("Respuesta correcta:".length()).trim();

                            // Modificar aquí para solo guardar la letra (sin el símbolo ')')
                            if (respuestaCorrecta.length() > 1) {
                                respuestaCorrecta = respuestaCorrecta.substring(0, 1); // Guardamos solo la letra
                            }
                        }
                    }

                    alternativasList.add(alternativas); // Agrega todas las alternativas a la lista

                    if (respuestaCorrecta != null) {
                        respuestasCorrectasMap.put("Respuesta " + questionCounter, respuestaCorrecta);
                    }

                    questionCounter++;
                }
            }


            // Convertir los mapas a JSON
            String preguntasJson = objectMapper.writeValueAsString(preguntasMap);
            String respuestasCorrectasJson = objectMapper.writeValueAsString(respuestasCorrectasMap);

            // Guardar Pregunta
            Pregunta pregunta = new Pregunta(preguntasJson);
            pregunta = preguntaRepository.save(pregunta);

            // Guardar todas las alternativas en un solo registro JSON
            String alternativasJson = objectMapper.writeValueAsString(alternativasList);
            Alternativa alternativaEntity = new Alternativa(alternativasJson, pregunta);
            pregunta.getAlternativas().add(alternativaEntity);

            // Guardar Respuesta Correcta (solo la letra de la respuesta correcta)
            RespuestaCorrecta respuestaCorrectaEntity = new RespuestaCorrecta(respuestasCorrectasJson, pregunta);
            pregunta.setRespuestaCorrecta(respuestaCorrectaEntity);

            // Guardar todas las entidades relacionadas
            preguntaRepository.save(pregunta);

            // Retornar la pregunta con alternativas y respuesta correcta
            return List.of(pregunta); // Aquí retornamos la pregunta recién creada
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new RuntimeException("Demasiadas solicitudes, por favor intenta más tarde.");
            }
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la respuesta de la API", e);
        }
    }
}
