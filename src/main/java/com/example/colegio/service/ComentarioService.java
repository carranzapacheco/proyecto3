package com.example.colegio.service;

import com.example.colegio.model.Comentario;
import com.example.colegio.repository.ComentarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ComentarioService { //acá está  toda la lógica

    @Autowired
    private ComentarioRepository repo;

    public List<Comentario> listar() {
        return repo.findAll();
    }

    public Comentario guardar(Comentario comentario) {
        return repo.save(comentario);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

}
