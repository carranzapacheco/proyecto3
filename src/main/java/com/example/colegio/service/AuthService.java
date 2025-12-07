package com.example.colegio.service;

import com.example.colegio.model.Usuario;
import com.example.colegio.model.Alumno;
import com.example.colegio.model.Docente;
import com.example.colegio.repository.UsuarioRepository;
import com.example.colegio.repository.AlumnoRepository;
import com.example.colegio.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** Registrar un nuevo usuario */
    public Usuario registrarUsuario(String email, String contrasena, Usuario.Rol rol) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            return null;
        }

        Usuario nuevo = new Usuario();
        nuevo.setEmail(email);
        nuevo.setContrasena(passwordEncoder.encode(contrasena));
        nuevo.setRol(rol); // ALUMNO / DOCENTE / ADMIN

        return usuarioRepository.save(nuevo);
    }


    /** Verificar si el email ya está registrado */
    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    /**
    Iniciar sesión (comparando contraseñas cifradas)*/
    public Usuario login(String email, String contrasena) {
        Optional<Usuario> optUser = usuarioRepository.findByEmail(email);
        if (optUser.isEmpty()) return null;

        Usuario u = optUser.get();
        if (passwordEncoder.matches(contrasena, u.getContrasena())) {
            return u;
        }
        return null;
    }

    /**
     Crear perfil de alumno asociado al usuario*/
    public Alumno crearAlumno(String nombre, String apellido, String grado, Usuario usuario) {
        Alumno a = new Alumno();
        a.setNombre(nombre);
        a.setApellido(apellido);
        a.setGrado(grado);
        a.setUsuario(usuario);
        return alumnoRepository.save(a);
    }

    /**
     Crear perfil de docente asociado al usuario*/
    public Docente crearDocente(String nombre, String apellido, String especialidad, Usuario usuario) {
        Docente d = new Docente();
        d.setNombre(nombre);
        d.setApellido(apellido);
        d.setEspecialidad(especialidad);
        d.setUsuario(usuario);
        return docenteRepository.save(d);
    }

    public void crearAdminSiNoExiste() {
        if (usuarioRepository.findByEmail("admin@colegio.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail("admin@colegio.com");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(Usuario.Rol.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("Nuevo administrador por defecto creado.");
        }
    }

}
