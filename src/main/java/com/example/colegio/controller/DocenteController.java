package com.example.colegio.controller;

import com.example.colegio.service.AlumnoService;
import com.example.colegio.model.Docente;
import com.example.colegio.model.Usuario;
import com.example.colegio.service.DocenteService;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private AlumnoService alumnoService;

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

        return "sesiones/docente";
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

        // Subir foto
        if (foto != null && !foto.isEmpty()) {
            try {
                String rutaUploads = new File("src/main/resources/static/uploads").getAbsolutePath();
                Path rutaCarpeta = Paths.get(rutaUploads);

                if (!Files.exists(rutaCarpeta)) {
                    Files.createDirectories(rutaCarpeta);
                }

                String nombreLimpio = Paths.get(foto.getOriginalFilename()).getFileName().toString();
                String nombreArchivo = "docente_" + docente.getId() + "_" + nombreLimpio;
                Path rutaArchivo = rutaCarpeta.resolve(nombreArchivo);

                foto.transferTo(rutaArchivo.toFile());

                docente.setFotoUrl("/uploads/" + nombreArchivo);
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
