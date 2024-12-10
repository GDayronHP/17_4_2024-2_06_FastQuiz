package com.secure.fastquiz.controllers;

import com.secure.fastquiz.dtos.CuestionarioDTO;
import com.secure.fastquiz.dtos.RespuestaCuestionarioDTO;
import com.secure.fastquiz.dtos.ResultadoComparacionDTO;
import com.secure.fastquiz.models.Cuestionario;
import com.secure.fastquiz.models.PublicacionCuestionario;
import com.secure.fastquiz.service.CuestionarioService;
import com.secure.fastquiz.service.RespuestaCuestionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cuestionarios")
public class CuestionarioController {

    @Autowired
    private CuestionarioService cuestionarioService;

    @Autowired
    private RespuestaCuestionarioService respuestaCuestionarioService;

    @PostMapping("/crear")
    public ResponseEntity<Cuestionario> crearCuestionario(@RequestBody Map<String, Object> request,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        String titulo = (String) request.get("titulo");
        String descripcion = (String) request.get("descripcion");
        Long preguntaId = ((Number) request.get("preguntaId")).longValue(); // ID de la pregunta que contiene el JSON

        // Lista de claves de preguntas específicas dentro del JSON
        List<String> clavesPreguntas = (List<String>) request.get("clavesPreguntas");

        // Lista de tiempos y requeridos para cada pregunta
        List<Integer> tiempos = (List<Integer>) request.get("tiempos");
        List<Boolean> requeridos = (List<Boolean>) request.get("requeridos");

        Cuestionario cuestionario = cuestionarioService.crearCuestionario(titulo, descripcion, preguntaId, clavesPreguntas, tiempos, requeridos);
        return new ResponseEntity<>(cuestionario, HttpStatus.CREATED);
    }

    @GetMapping("/cuestions/{id}")
    public ResponseEntity<CuestionarioDTO> obtenerCuestionario(@PathVariable Long id,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        CuestionarioDTO cuestionarioDTO = cuestionarioService.obtenerCuestionarioConPreguntasYAlternativas(id);
        return new ResponseEntity<>(cuestionarioDTO, HttpStatus.OK);
    }

    @PutMapping("/editCuestion/{id}/editar")
    public ResponseEntity<Cuestionario> editarCuestionario(@PathVariable Long id,
                                                           @RequestBody Map<String, Object> request,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        String nuevoTitulo = (String) request.get("titulo");
        String nuevaDescripcion = (String) request.get("descripcion");
        List<Long> preguntasSeleccionadasIds = (List<Long>) request.get("preguntasSeleccionadasIds");
        List<Integer> nuevosTiempos = (List<Integer>) request.get("tiempos");
        List<Boolean> nuevosRequeridos = (List<Boolean>) request.get("requeridos");

        Cuestionario cuestionarioActualizado = cuestionarioService.editarCuestionario(id, nuevoTitulo, nuevaDescripcion, preguntasSeleccionadasIds, nuevosTiempos, nuevosRequeridos);
        return new ResponseEntity<>(cuestionarioActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/deletCuestion/{id}/eliminar")
    public ResponseEntity<Void> eliminarCuestionario(@PathVariable Long id,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        cuestionarioService.eliminarCuestionario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/pregunta/{id}/eliminar")
    public ResponseEntity<Void> eliminarPreguntaSeleccionada(@PathVariable Long id,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        cuestionarioService.eliminarPreguntaSeleccionada(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/publicar/{cuestionarioId}")
    public ResponseEntity<PublicacionCuestionario> publicarCuestionario(
            @PathVariable Long cuestionarioId,
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        LocalDateTime fechaInicio = LocalDateTime.parse((String) request.get("fechaInicio"));
        LocalDateTime fechaFin = LocalDateTime.parse((String) request.get("fechaFin"));
        String urlPublica = (String) request.get("urlPublica");

        PublicacionCuestionario publicacion = cuestionarioService.publicarCuestionario(cuestionarioId, fechaInicio, fechaFin, urlPublica);
        return new ResponseEntity<>(publicacion, HttpStatus.CREATED);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarRespuestas(@RequestBody RespuestaCuestionarioDTO respuestaCuestionarioDTO,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        // Verificar si el usuario está autenticado
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Si no está autenticado
        }

        // Llamar al servicio para registrar las respuestas
        respuestaCuestionarioService.registrarRespuestas(respuestaCuestionarioDTO);

        // Retornar una respuesta vacía con un código de estado 200 OK
        return ResponseEntity.ok().build();
    }

    //------------------------------------------------------
    // Endpoint para obtener las respuestas por cuestionario
    @GetMapping("/cuestionario/{cuestionarioId}")
    public ResponseEntity<List<RespuestaCuestionarioDTO.PreguntaRespuestaDTO>> obtenerRespuestasPorCuestionario(
            @PathVariable Long cuestionarioId,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<RespuestaCuestionarioDTO.PreguntaRespuestaDTO> respuestas = respuestaCuestionarioService.obtenerRespuestasPorCuestionario(cuestionarioId);
        return new ResponseEntity<>(respuestas, HttpStatus.OK);
    }

    //Metodo para obtener el resultado del cuestionario----------------usar para la retroalimentación-------
    @GetMapping("/comparar-cuestionario/{cuestionarioId}")
    public ResponseEntity<List<ResultadoComparacionDTO>> compararCuestionario(
            @PathVariable Long cuestionarioId,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<ResultadoComparacionDTO> resultados = respuestaCuestionarioService.compararRespuestasConCorrectas(cuestionarioId);
        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

}

