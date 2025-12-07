package com.example.colegio.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.colegio.model.Docente;
import com.example.colegio.repository.DocenteRepository;

@Service
public class DocenteService { //acá está  toda la lógica

    @Autowired
    private DocenteRepository docenteRepository;

    public Docente buscarPorEmail(String email) {
        return docenteRepository.findByUsuarioEmail(email);
    }

    public List<Docente> obtenerTodos() {
        return docenteRepository.findAll();
    }

        public void guardar(Docente docente) {
        docenteRepository.save(docente);
    }

    public void eliminar(Long id) {
    Docente docente = docenteRepository.findById(id).orElse(null);
        if (docente != null) {
            docente.getAlumnos().forEach(a -> a.getDocentes().remove(docente));
            docenteRepository.delete(docente);
        }
    }

}
