package com.example.colegio.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/redirigir")
    public String redirigirSegunRol(Authentication auth) {

        String rol = auth.getAuthorities().iterator().next().getAuthority();

        return switch (rol) {
            case "ROLE_ADMIN" -> "redirect:/admin/dashboard";
            case "ROLE_DOCENTE" -> "redirect:/docente/inicio";
            default -> "redirect:/alumno/inicio";
        };
    }
}
