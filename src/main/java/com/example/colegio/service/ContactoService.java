package com.example.colegio.service;

import com.example.colegio.model.Contacto;
import com.example.colegio.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoService { //acá está  toda la lógica

    @Autowired
    private ContactoRepository repo;

    // Listar todos los contactos
    public List<Contacto> listar() {
        return repo.findAll();
    }

    // Guardar un contacto
    public Contacto guardar(Contacto contacto) {
        return repo.save(contacto);
    }
}
