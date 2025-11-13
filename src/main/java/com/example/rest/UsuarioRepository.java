package com.example.rest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria para gestionar usuarios.
 * Utiliza ConcurrentHashMap para thread-safety.
 */
public class UsuarioRepository {
    private static final UsuarioRepository instance = new UsuarioRepository();
    private final Map<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private UsuarioRepository() {
        // Inicializar con algunos datos de ejemplo
        inicializarDatosEjemplo();
    }

    public static UsuarioRepository getInstance() {
        return instance;
    }

    /**
     * Inicializa algunos datos de ejemplo.
     */
    private void inicializarDatosEjemplo() {
        crear(new Usuario(null, "Juan", "Pérez", "juan.perez@example.com", 30, "+1234567890", "Calle Principal 123"));
        crear(new Usuario(null, "María", "González", "maria.gonzalez@example.com", 25, "+0987654321", "Avenida Central 456"));
        crear(new Usuario(null, "Carlos", "Rodríguez", "carlos.rodriguez@example.com", 35, "+1122334455", "Plaza Mayor 789"));
    }

    /**
     * Obtiene todos los usuarios.
     */
    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios.values());
    }

    /**
     * Busca un usuario por ID.
     */
    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    /**
     * Busca usuarios por nombre (búsqueda parcial, case-insensitive).
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return obtenerTodos();
        }
        String nombreLower = nombre.toLowerCase();
        return usuarios.values().stream()
                .filter(u -> u.getNombre() != null && u.getNombre().toLowerCase().contains(nombreLower))
                .collect(Collectors.toList());
    }

    /**
     * Busca usuarios por email.
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.values().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    /**
     * Crea un nuevo usuario y le asigna un ID.
     */
    public Usuario crear(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        
        // Verificar si el email ya existe
        if (usuario.getEmail() != null && buscarPorEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getEmail());
        }

        Long nuevoId = idGenerator.getAndIncrement();
        usuario.setId(nuevoId);
        usuarios.put(nuevoId, usuario);
        return usuario;
    }

    /**
     * Actualiza un usuario existente.
     */
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        if (usuarioActualizado == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        Usuario usuarioExistente = usuarios.get(id);
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + id);
        }

        // Verificar si el email está siendo cambiado y si ya existe
        if (usuarioActualizado.getEmail() != null && 
            !usuarioActualizado.getEmail().equalsIgnoreCase(usuarioExistente.getEmail())) {
            if (buscarPorEmail(usuarioActualizado.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuarioActualizado.getEmail());
            }
        }

        // Actualizar campos
        usuarioActualizado.setId(id);
        usuarios.put(id, usuarioActualizado);
        return usuarioActualizado;
    }

    /**
     * Elimina un usuario por ID.
     */
    public boolean eliminar(Long id) {
        if (id == null) {
            return false;
        }
        return usuarios.remove(id) != null;
    }

    /**
     * Obtiene el número total de usuarios.
     */
    public int contar() {
        return usuarios.size();
    }
}

