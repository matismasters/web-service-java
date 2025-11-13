package com.example.rest;

import java.util.Objects;

/**
 * Clase que representa una relación de amistad entre dos usuarios.
 */
public class Amistad {
    private Long id;
    private Long usuarioId1;
    private Long usuarioId2;
    private String fechaAmistad; // Fecha en formato String (simplificado)

    public Amistad() {
    }

    public Amistad(Long id, Long usuarioId1, Long usuarioId2, String fechaAmistad) {
        this.id = id;
        this.usuarioId1 = usuarioId1;
        this.usuarioId2 = usuarioId2;
        this.fechaAmistad = fechaAmistad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId1() {
        return usuarioId1;
    }

    public void setUsuarioId1(Long usuarioId1) {
        this.usuarioId1 = usuarioId1;
    }

    public Long getUsuarioId2() {
        return usuarioId2;
    }

    public void setUsuarioId2(Long usuarioId2) {
        this.usuarioId2 = usuarioId2;
    }

    public String getFechaAmistad() {
        return fechaAmistad;
    }

    public void setFechaAmistad(String fechaAmistad) {
        this.fechaAmistad = fechaAmistad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amistad amistad = (Amistad) o;
        return Objects.equals(id, amistad.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

