package com.example.colegio.controller;

import com.example.colegio.model.Contacto;
import com.example.colegio.service.ContactoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    // Listar todos los contactos
    @GetMapping
    public List<Contacto> listar() {
        return contactoService.listar();
    }

    // Guardar contacto con validación
    @PostMapping
    public Object guardar(@Valid @RequestBody Contacto contacto, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return errores;
        }
        return contactoService.guardar(contacto);
    }
}
