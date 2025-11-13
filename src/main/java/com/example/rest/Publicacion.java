package com.example.rest;

import java.util.Objects;

/**
 * Clase que representa una publicación de un usuario.
 */
public class Publicacion {
    private Long id;
    private Long usuarioId;
    private String contenido;
    private String fechaCreacion;
    private Integer likes;

    public Publicacion() {
        this.likes = 0;
    }

    public Publicacion(Long id, Long usuarioId, String contenido, String fechaCreacion, Integer likes) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
        this.likes = likes != null ? likes : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes != null ? likes : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publicacion that = (Publicacion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

