package com.example.rest;

import java.util.Objects;

/**
 * Clase que representa un comentario en una publicación.
 */
public class Comentario {
    private Long id;
    private Long publicacionId;
    private Long usuarioId;
    private String contenido;
    private String fechaCreacion;

    public Comentario() {
    }

    public Comentario(Long id, Long publicacionId, Long usuarioId, String contenido, String fechaCreacion) {
        this.id = id;
        this.publicacionId = publicacionId;
        this.usuarioId = usuarioId;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Long publicacionId) {
        this.publicacionId = publicacionId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comentario that = (Comentario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

