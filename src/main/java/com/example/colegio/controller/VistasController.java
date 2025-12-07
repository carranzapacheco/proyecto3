package com.example.colegio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistasController {

    
    @GetMapping("/contacto")
    public String contacto() {
        return "vistas/contacto"; 
    }

    @GetMapping("/noticias")
    public String noticias() {
        return "vistas/noticias"; 
    }

    @GetMapping("/talleres")
    public String talleres() {
        return "vistas/talleres"; 
    }
}

