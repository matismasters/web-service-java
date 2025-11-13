package com.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Recurso REST para gestionar usuarios.
 * Proporciona todas las operaciones CRUD (Create, Read, Update, Delete).
 */
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private final UsuarioRepository repository = UsuarioRepository.getInstance();

    /**
     * GET /api/usuarios
     * Obtiene todos los usuarios.
     * 
     * Query params opcionales:
     * - nombre: filtra usuarios por nombre (búsqueda parcial)
     */
    @GET
    public Response obtenerTodos(@QueryParam("nombre") String nombre) {
        try {
            List<Usuario> usuarios;
            if (nombre != null && !nombre.trim().isEmpty()) {
                usuarios = repository.buscarPorNombre(nombre);
            } else {
                usuarios = repository.obtenerTodos();
            }
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener usuarios: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/usuarios/{id}
     * Obtiene un usuario por ID.
     */
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        try {
            return repository.buscarPorId(id)
                    .map(usuario -> Response.ok(usuario).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse("Usuario no encontrado con ID: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener usuario: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * POST /api/usuarios
     * Crea un nuevo usuario.
     */
    @POST
    public Response crear(Usuario usuario) {
        try {
            if (usuario == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El usuario no puede ser nulo"))
                        .build();
            }

            // Validaciones básicas
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El nombre es obligatorio"))
                        .build();
            }

            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El email es obligatorio"))
                        .build();
            }

            Usuario usuarioCreado = repository.crear(usuario);
            return Response.status(Response.Status.CREATED)
                    .entity(usuarioCreado)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al crear usuario: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * PUT /api/usuarios/{id}
     * Actualiza un usuario existente.
     */
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, Usuario usuario) {
        try {
            if (usuario == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El usuario no puede ser nulo"))
                        .build();
            }

            // Validaciones básicas
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El nombre es obligatorio"))
                        .build();
            }

            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El email es obligatorio"))
                        .build();
            }

            Usuario usuarioActualizado = repository.actualizar(id, usuario);
            return Response.ok(usuarioActualizado).build();
        } catch (IllegalArgumentException e) {
            Response.Status status = e.getMessage().contains("no encontrado") 
                    ? Response.Status.NOT_FOUND 
                    : Response.Status.BAD_REQUEST;
            return Response.status(status)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al actualizar usuario: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * DELETE /api/usuarios/{id}
     * Elimina un usuario por ID.
     */
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        try {
            boolean eliminado = repository.eliminar(id);
            if (eliminado) {
                return Response.ok(new MessageResponse("Usuario eliminado correctamente")).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Usuario no encontrado con ID: " + id))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al eliminar usuario: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/usuarios/contar
     * Obtiene el número total de usuarios.
     */
    @GET
    @Path("/contar")
    public Response contar() {
        try {
            int total = repository.contar();
            return Response.ok(new CountResponse(total)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al contar usuarios: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Clase interna para respuestas de error.
     */
    public static class ErrorResponse {
        private String error;

        public ErrorResponse() {
        }

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    /**
     * Clase interna para respuestas de mensaje.
     */
    public static class MessageResponse {
        private String message;

        public MessageResponse() {
        }

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * Clase interna para respuestas de conteo.
     */
    public static class CountResponse {
        private int total;

        public CountResponse() {
        }

        public CountResponse(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}

