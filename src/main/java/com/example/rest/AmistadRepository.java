package com.example.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria para gestionar relaciones de amistad.
 */
public class AmistadRepository {
    private static final AmistadRepository instance = new AmistadRepository();
    private final Map<Long, Amistad> amistades = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AmistadRepository() {
    }

    public static AmistadRepository getInstance() {
        return instance;
    }

    /**
     * Crea una nueva relación de amistad entre dos usuarios.
     */
    public Amistad crearAmistad(Long usuarioId1, Long usuarioId2) {
        if (usuarioId1 == null || usuarioId2 == null) {
            throw new IllegalArgumentException("Los IDs de usuario no pueden ser nulos");
        }

        if (usuarioId1.equals(usuarioId2)) {
            throw new IllegalArgumentException("Un usuario no puede ser amigo de sí mismo");
        }

        // Verificar que ambos usuarios existen
        if (!usuarioRepository.buscarPorId(usuarioId1).isPresent()) {
            throw new IllegalArgumentException("Usuario con ID " + usuarioId1 + " no encontrado");
        }
        if (!usuarioRepository.buscarPorId(usuarioId2).isPresent()) {
            throw new IllegalArgumentException("Usuario con ID " + usuarioId2 + " no encontrado");
        }

        // Verificar si ya son amigos (en cualquier dirección)
        if (sonAmigos(usuarioId1, usuarioId2)) {
            throw new IllegalArgumentException("Los usuarios ya son amigos");
        }

        Long nuevoId = idGenerator.getAndIncrement();
        String fechaActual = LocalDateTime.now().format(formatter);
        Amistad amistad = new Amistad(nuevoId, usuarioId1, usuarioId2, fechaActual);
        amistades.put(nuevoId, amistad);
        return amistad;
    }

    /**
     * Verifica si dos usuarios son amigos.
     */
    public boolean sonAmigos(Long usuarioId1, Long usuarioId2) {
        return amistades.values().stream()
                .anyMatch(a -> (a.getUsuarioId1().equals(usuarioId1) && a.getUsuarioId2().equals(usuarioId2)) ||
                               (a.getUsuarioId1().equals(usuarioId2) && a.getUsuarioId2().equals(usuarioId1)));
    }

    /**
     * Obtiene todos los amigos de un usuario.
     */
    public List<Long> obtenerAmigosIds(Long usuarioId) {
        return amistades.values().stream()
                .filter(a -> a.getUsuarioId1().equals(usuarioId) || a.getUsuarioId2().equals(usuarioId))
                .map(a -> a.getUsuarioId1().equals(usuarioId) ? a.getUsuarioId2() : a.getUsuarioId1())
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las amistades de un usuario (objetos Amistad completos).
     */
    public List<Amistad> obtenerAmistades(Long usuarioId) {
        return amistades.values().stream()
                .filter(a -> a.getUsuarioId1().equals(usuarioId) || a.getUsuarioId2().equals(usuarioId))
                .collect(Collectors.toList());
    }

    /**
     * Elimina una relación de amistad entre dos usuarios.
     */
    public boolean eliminarAmistad(Long usuarioId1, Long usuarioId2) {
        Optional<Amistad> amistad = amistades.values().stream()
                .filter(a -> (a.getUsuarioId1().equals(usuarioId1) && a.getUsuarioId2().equals(usuarioId2)) ||
                             (a.getUsuarioId1().equals(usuarioId2) && a.getUsuarioId2().equals(usuarioId1)))
                .findFirst();

        if (amistad.isPresent()) {
            amistades.remove(amistad.get().getId());
            return true;
        }
        return false;
    }

    /**
     * Obtiene todas las amistades.
     */
    public List<Amistad> obtenerTodas() {
        return new ArrayList<>(amistades.values());
    }
}

