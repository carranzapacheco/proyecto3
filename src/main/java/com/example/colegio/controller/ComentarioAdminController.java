package com.example.colegio.controller;

import com.example.colegio.model.Comentario;
import com.example.colegio.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/comentarios")
@CrossOrigin(origins = "*")
public class ComentarioAdminController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public List<Comentario> listar() {
        return comentarioService.listar();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        comentarioService.eliminar(id);
    }
}
