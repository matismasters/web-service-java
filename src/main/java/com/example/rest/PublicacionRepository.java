package com.example.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria para gestionar publicaciones.
 */
public class PublicacionRepository {
    private static final PublicacionRepository instance = new PublicacionRepository();
    private final Map<Long, Publicacion> publicaciones = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private PublicacionRepository() {
    }

    public static PublicacionRepository getInstance() {
        return instance;
    }

    /**
     * Crea una nueva publicación.
     */
    public Publicacion crear(Publicacion publicacion) {
        if (publicacion == null) {
            throw new IllegalArgumentException("La publicación no puede ser nula");
        }

        if (publicacion.getUsuarioId() == null) {
            throw new IllegalArgumentException("El ID de usuario es obligatorio");
        }

        // Verificar que el usuario existe
        if (!usuarioRepository.buscarPorId(publicacion.getUsuarioId()).isPresent()) {
            throw new IllegalArgumentException("Usuario con ID " + publicacion.getUsuarioId() + " no encontrado");
        }

        if (publicacion.getContenido() == null || publicacion.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido de la publicación no puede estar vacío");
        }

        Long nuevoId = idGenerator.getAndIncrement();
        publicacion.setId(nuevoId);
        publicacion.setFechaCreacion(LocalDateTime.now().format(formatter));
        if (publicacion.getLikes() == null) {
            publicacion.setLikes(0);
        }
        publicaciones.put(nuevoId, publicacion);
        return publicacion;
    }

    /**
     * Obtiene todas las publicaciones.
     */
    public List<Publicacion> obtenerTodas() {
        return new ArrayList<>(publicaciones.values());
    }

    /**
     * Obtiene una publicación por ID.
     */
    public Optional<Publicacion> buscarPorId(Long id) {
        return Optional.ofNullable(publicaciones.get(id));
    }

    /**
     * Obtiene todas las publicaciones de un usuario específico.
     */
    public List<Publicacion> obtenerPorUsuarioId(Long usuarioId) {
        return publicaciones.values().stream()
                .filter(p -> p.getUsuarioId().equals(usuarioId))
                .sorted((p1, p2) -> p2.getFechaCreacion().compareTo(p1.getFechaCreacion())) // Más recientes primero
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las publicaciones de los amigos de un usuario.
     */
    public List<Publicacion> obtenerPublicacionesAmigos(Long usuarioId) {
        AmistadRepository amistadRepo = AmistadRepository.getInstance();
        List<Long> amigosIds = amistadRepo.obtenerAmigosIds(usuarioId);
        
        return publicaciones.values().stream()
                .filter(p -> amigosIds.contains(p.getUsuarioId()))
                .sorted((p1, p2) -> p2.getFechaCreacion().compareTo(p1.getFechaCreacion()))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una publicación.
     */
    public Publicacion actualizar(Long id, Publicacion publicacionActualizada) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        Publicacion publicacionExistente = publicaciones.get(id);
        if (publicacionExistente == null) {
            throw new IllegalArgumentException("Publicación no encontrada con ID: " + id);
        }

        if (publicacionActualizada.getContenido() == null || publicacionActualizada.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido de la publicación no puede estar vacío");
        }

        publicacionActualizada.setId(id);
        publicacionActualizada.setUsuarioId(publicacionExistente.getUsuarioId()); // No se puede cambiar el autor
        publicacionActualizada.setFechaCreacion(publicacionExistente.getFechaCreacion()); // Mantener fecha original
        if (publicacionActualizada.getLikes() == null) {
            publicacionActualizada.setLikes(publicacionExistente.getLikes());
        }
        publicaciones.put(id, publicacionActualizada);
        return publicacionActualizada;
    }

    /**
     * Elimina una publicación.
     */
    public boolean eliminar(Long id) {
        if (id == null) {
            return false;
        }
        return publicaciones.remove(id) != null;
    }

    /**
     * Incrementa los likes de una publicación.
     */
    public Publicacion darLike(Long id) {
        Publicacion publicacion = publicaciones.get(id);
        if (publicacion == null) {
            throw new IllegalArgumentException("Publicación no encontrada con ID: " + id);
        }
        publicacion.setLikes(publicacion.getLikes() + 1);
        return publicacion;
    }

    /**
     * Decrementa los likes de una publicación.
     */
    public Publicacion quitarLike(Long id) {
        Publicacion publicacion = publicaciones.get(id);
        if (publicacion == null) {
            throw new IllegalArgumentException("Publicación no encontrada con ID: " + id);
        }
        if (publicacion.getLikes() > 0) {
            publicacion.setLikes(publicacion.getLikes() - 1);
        }
        return publicacion;
    }
}

