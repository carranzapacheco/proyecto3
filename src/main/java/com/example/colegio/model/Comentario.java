package com.example.colegio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comentario { //genera una tabla en la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //validaciones
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 80, message = "El nombre debe tener mínimo 2 caracteres.")
    private String nombre;

    @NotBlank(message = "El texto no puede estar vacío")
    @Size(min = 10, max = 1000, message = "El mensaje debe tener entre 10 y 1000 caracteres.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    private LocalDateTime fecha = LocalDateTime.now();
}
