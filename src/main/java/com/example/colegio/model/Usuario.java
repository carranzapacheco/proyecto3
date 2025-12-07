package com.example.colegio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario { //genera una tabla en la base de datos

    public enum Rol { ALUMNO, DOCENTE, ADMIN } //los roles

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=100)
    private String email;

    @Column(nullable=false, length=255)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Rol rol;

    // getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
