package com.example.colegio.repository;

import com.example.colegio.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

//accede a la base de datos y proporciona los métodos CRUD
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Docente findByUsuarioEmail(String email);
}

