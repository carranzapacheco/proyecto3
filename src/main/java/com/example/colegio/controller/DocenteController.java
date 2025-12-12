package com.example.colegio.controller;

import com.example.colegio.service.AlumnoService;
import com.example.colegio.service.CloudinaryService;
import com.example.colegio.model.Docente;
import com.example.colegio.model.Usuario;
import com.example.colegio.service.DocenteService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private CloudinaryService cloudinaryService;

    // PÁGINA PRINCIPAL DEL DOCENTE
    @GetMapping("/docente")
    public String docenteInicio(Model model, HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Docente docente = docenteService.buscarPorEmail(usuario.getEmail());
        model.addAttribute("docente", docente);

        model.addAttribute("alumnos", alumnoService.obtenerTodos());

        return "sesiones/Docente";
    }

    // PERFIL DEL DOCENTE
    @GetMapping("/docente/perfil")
    public String verPerfilDocente(Model model, HttpSession session, @ModelAttribute("mensaje") String mensaje) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/docente";
        }

        Docente docente = docenteService.buscarPorEmail(usuario.getEmail());
        model.addAttribute("docente", docente);
        model.addAttribute("usuario", docente.getUsuario());

        if (mensaje != null && !mensaje.isEmpty()) {
            model.addAttribute("mensaje", mensaje);
        }

        return "sesiones/perfil-docente";
    }

    // ACTUALIZAR PERFIL DEL DOCENTE
    @PostMapping("/docente/perfil/actualizar")
    public String actualizarPerfilDocente(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("especialidad") String especialidad,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            HttpSession session,
            RedirectAttributes redirectAttrs) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/docente";
        }

        Docente docente = docenteService.buscarPorEmail(usuario.getEmail());
        docente.setNombre(nombre);
        docente.setApellido(apellido);
        docente.setEspecialidad(especialidad);

        // Subir foto a Cloudinary
        if (foto != null && !foto.isEmpty()) {
            try {
                String publicId = "docente_" + docente.getId();
                String url = cloudinaryService.upload(foto, publicId);
                docente.setFotoUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        docenteService.guardar(docente);
        redirectAttrs.addFlashAttribute("mensaje", "✅ Perfil actualizado correctamente");

        return "redirect:/docente/perfil";
    }

    // API REST — Docente por email
    @GetMapping("/api/docentes/email/{email}")
    @ResponseBody
    public Docente obtenerDocentePorEmail(@PathVariable String email) {
        return docenteService.buscarPorEmail(email);
    }

    // API REST — Listar docentes
    @GetMapping("/api/docentes")
    @ResponseBody
    public Iterable<Docente> listarDocentes() {
        return docenteService.obtenerTodos();
    }

}
