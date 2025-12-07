package com.example.colegio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.colegio.model.Comentario;

//accede a la base de datos y proporciona los métodos CRUD
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}
