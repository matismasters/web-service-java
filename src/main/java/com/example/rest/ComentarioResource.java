package com.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Recurso REST para gestionar comentarios en publicaciones.
 */
@Path("/comentarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComentarioResource {

    private final ComentarioRepository comentarioRepository = ComentarioRepository.getInstance();
    private final PublicacionRepository publicacionRepository = PublicacionRepository.getInstance();
    private final UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();

    /**
     * GET /api/comentarios/publicacion/{publicacionId}
     * Obtiene todos los comentarios de una publicación.
     */
    @GET
    @Path("/publicacion/{publicacionId}")
    public Response obtenerPorPublicacion(@PathParam("publicacionId") Long publicacionId) {
        try {
            if (!publicacionRepository.buscarPorId(publicacionId).isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Publicación no encontrada con ID: " + publicacionId))
                        .build();
            }

            List<Comentario> comentarios = comentarioRepository.obtenerPorPublicacionId(publicacionId);
            return Response.ok(comentarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener comentarios: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/comentarios/{id}
     * Obtiene un comentario por ID.
     */
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        try {
            return comentarioRepository.buscarPorId(id)
                    .map(comentario -> Response.ok(comentario).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse("Comentario no encontrado con ID: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener comentario: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/comentarios/usuario/{usuarioId}
     * Obtiene todos los comentarios de un usuario.
     */
    @GET
    @Path("/usuario/{usuarioId}")
    public Response obtenerPorUsuario(@PathParam("usuarioId") Long usuarioId) {
        try {
            if (!usuarioRepository.buscarPorId(usuarioId).isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Usuario no encontrado con ID: " + usuarioId))
                        .build();
            }

            List<Comentario> comentarios = comentarioRepository.obtenerPorUsuarioId(usuarioId);
            return Response.ok(comentarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener comentarios: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * POST /api/comentarios
     * Crea un nuevo comentario en una publicación.
     */
    @POST
    public Response crear(Comentario comentario) {
        try {
            if (comentario == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El comentario no puede ser nulo"))
                        .build();
            }

            Comentario comentarioCreado = comentarioRepository.crear(comentario);
            return Response.status(Response.Status.CREATED)
                    .entity(comentarioCreado)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al crear comentario: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * PUT /api/comentarios/{id}
     * Actualiza un comentario existente.
     */
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, Comentario comentario) {
        try {
            if (comentario == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El comentario no puede ser nulo"))
                        .build();
            }

            Comentario comentarioActualizado = comentarioRepository.actualizar(id, comentario);
            return Response.ok(comentarioActualizado).build();
        } catch (IllegalArgumentException e) {
            Response.Status status = e.getMessage().contains("no encontrado") 
                    ? Response.Status.NOT_FOUND 
                    : Response.Status.BAD_REQUEST;
            return Response.status(status)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al actualizar comentario: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * DELETE /api/comentarios/{id}
     * Elimina un comentario.
     */
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        try {
            boolean eliminado = comentarioRepository.eliminar(id);
            if (eliminado) {
                return Response.ok(new MessageResponse("Comentario eliminado correctamente")).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Comentario no encontrado con ID: " + id))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al eliminar comentario: " + e.getMessage()))
                    .build();
        }
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

