package com.example.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase de aplicación JAX-RS que configura el path base para todos los endpoints REST.
 * Todos los endpoints estarán disponibles bajo el path /api
 */
@ApplicationPath("/api")
public class JaxRsApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // Registrar aquí todos los recursos REST
        classes.add(HelloResource.class);
        classes.add(UsuarioResource.class);
        return classes;
    }
}

