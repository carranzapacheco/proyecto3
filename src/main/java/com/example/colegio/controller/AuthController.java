package com.example.colegio.controller;

import com.example.colegio.model.Usuario;
import com.example.colegio.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()_+=-]).{8,}$");

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{3,}$");

    @GetMapping("/login")
    public String loginForm() {
        authService.crearAdminSiNoExiste();
        return "sesiones/login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("roles", Usuario.Rol.values());
        return "sesiones/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam String rol,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String grado,
            @RequestParam(required = false) String especialidad,
            Model model) {

        model.addAttribute("roles", Usuario.Rol.values());

        if (!email.endsWith("@colegio.ejemplar")) {
            model.addAttribute("error", "El correo debe terminar en @colegio.ejemplar");
            return "sesiones/registro";
        }

        if (nombre == null || !NAME_PATTERN.matcher(nombre).matches()) {
            model.addAttribute("error", "El nombre debe tener al menos 3 letras");
            return "sesiones/registro";
        }
        if (apellido == null || !NAME_PATTERN.matcher(apellido).matches()) {
            model.addAttribute("error", "El apellido debe tener al menos 3 letras");
            return "sesiones/registro";
        }

        if (!PASSWORD_PATTERN.matcher(contrasena).matches()) {
            model.addAttribute("error",
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un símbolo");
            return "sesiones/registro";
        }

        Usuario.Rol usuarioRol;
        try {
            usuarioRol = Usuario.Rol.valueOf(rol);
        } catch (Exception ex) {
            model.addAttribute("error", "Rol inválido");
            return "sesiones/registro";
        }

        if (authService.existeEmail(email)) {
            model.addAttribute("error", "Ya existe un usuario con ese correo");
            return "sesiones/registro";
        }

        Usuario nuevo = authService.registrarUsuario(email, contrasena, usuarioRol);

        if (usuarioRol == Usuario.Rol.ALUMNO) {
            if (grado == null || grado.trim().isEmpty()) {
                model.addAttribute("error", "Debe indicar el grado");
                return "sesiones/registro";
            }
            authService.crearAlumno(nombre, apellido, grado, nuevo);
        } else {
            if (especialidad == null || especialidad.trim().isEmpty()) {
                model.addAttribute("error", "Debe indicar la especialidad");
                return "sesiones/registro";
            }
            authService.crearDocente(nombre, apellido, especialidad, nuevo);
        }

        return "redirect:/login?registro=ok";
    }
}
