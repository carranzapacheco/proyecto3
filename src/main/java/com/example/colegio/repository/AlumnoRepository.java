package com.example.colegio.repository;


import com.example.colegio.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

//accede a la base de datos y proporciona los métodos CRUD
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Alumno findByUsuarioEmail(String email);
}
