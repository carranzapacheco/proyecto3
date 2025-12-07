package com.example.colegio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Contacto { //genera una tabla en la base de datos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //validaciones
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 80, message = "El nombre debe tener tener mínimo 2 caracteres.")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Debe ingresar un correo electrónico válido.")
    private String email;

    @NotBlank(message = "El asunto es obligatorio.")
    @Size(min = 3, max = 100, message = "El asunto debe tener entre 3 y 100 caracteres.")
    private String asunto;

    @NotBlank(message = "El mensaje no puede estar vacío.")
    @Size(min = 10, max = 1000, message = "El mensaje debe tener entre 10 y 1000 caracteres.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    private LocalDateTime fecha = LocalDateTime.now();
}
