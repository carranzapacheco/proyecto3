package com.example.colegio.controller;

import com.example.colegio.service.AlumnoService;
import com.example.colegio.service.DocenteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private DocenteService docenteService;

    // *** VISTA PRINCIPAL DEL ADMIN ***
    @GetMapping("")
    public String adminHome(Model model, Authentication auth) {

        model.addAttribute("admin", auth.getName());
        model.addAttribute("alumnos", alumnoService.obtenerTodos());
        model.addAttribute("docentes", docenteService.obtenerTodos());

        return "sesiones/admin"; 
    }

    // ELIMINAR ALUMNO (AJAX)
    @PostMapping("/eliminar-alumno/{id}")
    @ResponseBody
    public String eliminarAlumno(@PathVariable Long id) {
        alumnoService.eliminar(id);
        return "OK";
    }

    // ELIMINAR DOCENTE (AJAX)
    @PostMapping("/eliminar-docente/{id}")
    @ResponseBody
    public String eliminarDocente(@PathVariable Long id) {
        docenteService.eliminar(id);
        return "OK";
    }
}
