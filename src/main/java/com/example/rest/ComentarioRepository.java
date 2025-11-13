package com.example.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria para gestionar comentarios en publicaciones.
 */
public class ComentarioRepository {
    private static final ComentarioRepository instance = new ComentarioRepository();
    private final Map<Long, Comentario> comentarios = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();
    private final PublicacionRepository publicacionRepository = PublicacionRepository.getInstance();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ComentarioRepository() {
    }

    public static ComentarioRepository getInstance() {
        return instance;
    }

    /**
     * Crea un nuevo comentario en una publicación.
     */
    public Comentario crear(Comentario comentario) {
        if (comentario == null) {
            throw new IllegalArgumentException("El comentario no puede ser nulo");
        }

        if (comentario.getPublicacionId() == null) {
            throw new IllegalArgumentException("El ID de publicación es obligatorio");
        }

        if (comentario.getUsuarioId() == null) {
            throw new IllegalArgumentException("El ID de usuario es obligatorio");
        }

        // Verificar que la publicación existe
        if (!publicacionRepository.buscarPorId(comentario.getPublicacionId()).isPresent()) {
            throw new IllegalArgumentException("Publicación con ID " + comentario.getPublicacionId() + " no encontrada");
        }

        // Verificar que el usuario existe
        if (!usuarioRepository.buscarPorId(comentario.getUsuarioId()).isPresent()) {
            throw new IllegalArgumentException("Usuario con ID " + comentario.getUsuarioId() + " no encontrado");
        }

        if (comentario.getContenido() == null || comentario.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del comentario no puede estar vacío");
        }

        Long nuevoId = idGenerator.getAndIncrement();
        comentario.setId(nuevoId);
        comentario.setFechaCreacion(LocalDateTime.now().format(formatter));
        comentarios.put(nuevoId, comentario);
        return comentario;
    }

    /**
     * Obtiene todos los comentarios de una publicación.
     */
    public List<Comentario> obtenerPorPublicacionId(Long publicacionId) {
        return comentarios.values().stream()
                .filter(c -> c.getPublicacionId().equals(publicacionId))
                .sorted((c1, c2) -> c1.getFechaCreacion().compareTo(c2.getFechaCreacion())) // Más antiguos primero
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un comentario por ID.
     */
    public Optional<Comentario> buscarPorId(Long id) {
        return Optional.ofNullable(comentarios.get(id));
    }

    /**
     * Obtiene todos los comentarios de un usuario.
     */
    public List<Comentario> obtenerPorUsuarioId(Long usuarioId) {
        return comentarios.values().stream()
                .filter(c -> c.getUsuarioId().equals(usuarioId))
                .sorted((c1, c2) -> c2.getFechaCreacion().compareTo(c1.getFechaCreacion()))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un comentario.
     */
    public Comentario actualizar(Long id, Comentario comentarioActualizado) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        Comentario comentarioExistente = comentarios.get(id);
        if (comentarioExistente == null) {
            throw new IllegalArgumentException("Comentario no encontrado con ID: " + id);
        }

        if (comentarioActualizado.getContenido() == null || comentarioActualizado.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del comentario no puede estar vacío");
        }

        comentarioActualizado.setId(id);
        comentarioActualizado.setPublicacionId(comentarioExistente.getPublicacionId()); // No se puede cambiar la publicación
        comentarioActualizado.setUsuarioId(comentarioExistente.getUsuarioId()); // No se puede cambiar el autor
        comentarioActualizado.setFechaCreacion(comentarioExistente.getFechaCreacion()); // Mantener fecha original
        comentarios.put(id, comentarioActualizado);
        return comentarioActualizado;
    }

    /**
     * Elimina un comentario.
     */
    public boolean eliminar(Long id) {
        if (id == null) {
            return false;
        }
        return comentarios.remove(id) != null;
    }

    /**
     * Elimina todos los comentarios de una publicación.
     */
    public void eliminarPorPublicacionId(Long publicacionId) {
        List<Long> idsAEliminar = comentarios.values().stream()
                .filter(c -> c.getPublicacionId().equals(publicacionId))
                .map(Comentario::getId)
                .collect(Collectors.toList());
        
        idsAEliminar.forEach(comentarios::remove);
    }
}

