package com.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Recurso REST para gestionar publicaciones de usuarios.
 */
@Path("/publicaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PublicacionResource {

    private final PublicacionRepository publicacionRepository = PublicacionRepository.getInstance();
    private final UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();

    /**
     * GET /api/publicaciones
     * Obtiene todas las publicaciones.
     */
    @GET
    public Response obtenerTodas() {
        try {
            List<Publicacion> publicaciones = publicacionRepository.obtenerTodas();
            return Response.ok(publicaciones).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener publicaciones: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/publicaciones/{id}
     * Obtiene una publicación por ID.
     */
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        try {
            return publicacionRepository.buscarPorId(id)
                    .map(publicacion -> Response.ok(publicacion).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse("Publicación no encontrada con ID: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener publicación: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/publicaciones/usuario/{usuarioId}
     * Obtiene todas las publicaciones de un usuario específico.
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

            List<Publicacion> publicaciones = publicacionRepository.obtenerPorUsuarioId(usuarioId);
            return Response.ok(publicaciones).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener publicaciones: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/publicaciones/amigos/{usuarioId}
     * Obtiene las publicaciones de los amigos de un usuario.
     */
    @GET
    @Path("/amigos/{usuarioId}")
    public Response obtenerPublicacionesAmigos(@PathParam("usuarioId") Long usuarioId) {
        try {
            if (!usuarioRepository.buscarPorId(usuarioId).isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Usuario no encontrado con ID: " + usuarioId))
                        .build();
            }

            List<Publicacion> publicaciones = publicacionRepository.obtenerPublicacionesAmigos(usuarioId);
            return Response.ok(publicaciones).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener publicaciones de amigos: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * POST /api/publicaciones
     * Crea una nueva publicación.
     */
    @POST
    public Response crear(Publicacion publicacion) {
        try {
            if (publicacion == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("La publicación no puede ser nula"))
                        .build();
            }

            Publicacion publicacionCreada = publicacionRepository.crear(publicacion);
            return Response.status(Response.Status.CREATED)
                    .entity(publicacionCreada)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al crear publicación: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * PUT /api/publicaciones/{id}
     * Actualiza una publicación existente.
     */
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, Publicacion publicacion) {
        try {
            if (publicacion == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("La publicación no puede ser nula"))
                        .build();
            }

            Publicacion publicacionActualizada = publicacionRepository.actualizar(id, publicacion);
            return Response.ok(publicacionActualizada).build();
        } catch (IllegalArgumentException e) {
            Response.Status status = e.getMessage().contains("no encontrada") 
                    ? Response.Status.NOT_FOUND 
                    : Response.Status.BAD_REQUEST;
            return Response.status(status)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al actualizar publicación: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * DELETE /api/publicaciones/{id}
     * Elimina una publicación.
     */
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        try {
            // Eliminar también todos los comentarios de la publicación
            ComentarioRepository comentarioRepo = ComentarioRepository.getInstance();
            comentarioRepo.eliminarPorPublicacionId(id);

            boolean eliminada = publicacionRepository.eliminar(id);
            if (eliminada) {
                return Response.ok(new MessageResponse("Publicación eliminada correctamente")).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Publicación no encontrada con ID: " + id))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al eliminar publicación: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * POST /api/publicaciones/{id}/like
     * Incrementa los likes de una publicación.
     */
    @POST
    @Path("/{id}/like")
    public Response darLike(@PathParam("id") Long id) {
        try {
            Publicacion publicacion = publicacionRepository.darLike(id);
            return Response.ok(publicacion).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al dar like: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * DELETE /api/publicaciones/{id}/like
     * Decrementa los likes de una publicación.
     */
    @DELETE
    @Path("/{id}/like")
    public Response quitarLike(@PathParam("id") Long id) {
        try {
            Publicacion publicacion = publicacionRepository.quitarLike(id);
            return Response.ok(publicacion).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al quitar like: " + e.getMessage()))
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

