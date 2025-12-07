package com.example.colegio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.colegio.model.Alumno;
import com.example.colegio.repository.AlumnoRepository;

@Service
public class AlumnoService { //acá está  toda la lógica

    @Autowired
    private AlumnoRepository alumnoRepository;

    public Alumno buscarPorEmail(String email) {
        return alumnoRepository.findByUsuarioEmail(email);
    }

    @GetMapping
    public List<Alumno> obtenerTodos() {
        return alumnoRepository.findAll();
    }

    public Alumno guardar(Alumno alumno) {
    return alumnoRepository.save(alumno);
    }

    public void eliminar(Long id) {
    Alumno alumno = alumnoRepository.findById(id).orElse(null);
        if (alumno != null) {
            alumno.getDocentes().clear();        // limpia relaciones
            alumnoRepository.save(alumno);
            alumnoRepository.delete(alumno);
        }
    }

}

