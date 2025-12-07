package com.example.colegio.controller;

import com.example.colegio.model.Comentario;
import com.example.colegio.service.ComentarioService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comentario")
@CrossOrigin(origins = "*")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    // Listar todos los comentarios
    @GetMapping
    public List<Comentario> listar() {
        return comentarioService.listar();
    }

    // Guardar comentario con validación
    @PostMapping
    public Object guardar(@Valid @RequestBody Comentario comentario, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return errores;
        }
        return comentarioService.guardar(comentario);
    }

    // Eliminar comentario por ID
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        comentarioService.eliminar(id);
    }
}
