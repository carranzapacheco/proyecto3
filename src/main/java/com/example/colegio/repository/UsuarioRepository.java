package com.example.colegio.repository;

import com.example.colegio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

//accede a la base de datos y proporciona los métodos CRUD
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
