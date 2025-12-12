package com.example.colegio.controller;

import com.example.colegio.model.Alumno;
import com.example.colegio.model.Usuario;
import com.example.colegio.service.AlumnoService;
import com.example.colegio.service.CloudinaryService;
import com.example.colegio.service.DocenteService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private CloudinaryService cloudinaryService;

    // PÁGINA DE INICIO DEL ALUMNO
    @GetMapping("/alumno")
    public String alumnoInicio(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Alumno alumno = alumnoService.buscarPorEmail(usuario.getEmail());
        model.addAttribute("alumno", alumno);

        model.addAttribute("docentes", docenteService.obtenerTodos());

        return "sesiones/Alumno";
    }

    // PERFIL DEL ALUMNO
    @GetMapping("/alumno/perfil")
    public String verPerfil(Model model, HttpSession session, @ModelAttribute("mensaje") String mensaje) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/alumno";
        }

        Alumno alumno = alumnoService.buscarPorEmail(usuario.getEmail());
        model.addAttribute("alumno", alumno);

        if (mensaje != null && !mensaje.isEmpty()) {
            model.addAttribute("mensaje", mensaje);
        }

        return "sesiones/perfil";
    }

    // ACTUALIZAR PERFIL
    @PostMapping("/alumno/perfil/actualizar")
    public String actualizarPerfil(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("grado") String grado,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            HttpSession session,
            RedirectAttributes redirectAttrs) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/alumno";
        }

        Alumno alumno = alumnoService.buscarPorEmail(usuario.getEmail());
        alumno.setNombre(nombre);
        alumno.setApellido(apellido);
        alumno.setGrado(grado);

        // Subir foto a Cloudinary
        if (foto != null && !foto.isEmpty()) {
            try {
                String publicId = "alumno_" + alumno.getId(); // nombre en Cloudinary
                String url = cloudinaryService.upload(foto, publicId);
                alumno.setFotoUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        alumnoService.guardar(alumno);
        redirectAttrs.addFlashAttribute("mensaje", "✅ Perfil actualizado correctamente");

        return "redirect:/alumno/perfil";
    }

    // API REST
    @GetMapping("/api/alumnos/email/{email}")
    @ResponseBody
    public Alumno obtenerAlumnoPorEmail(@PathVariable String email) {
        return alumnoService.buscarPorEmail(email);
    }

    @GetMapping("/api/alumnos")
    @ResponseBody
    public Iterable<Alumno> listarAlumnos() {
        return alumnoService.obtenerTodos();
    }
}
