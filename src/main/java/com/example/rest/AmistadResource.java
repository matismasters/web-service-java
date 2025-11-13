package com.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recurso REST para gestionar relaciones de amistad entre usuarios.
 */
@Path("/amistades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AmistadResource {

    private final AmistadRepository amistadRepository = AmistadRepository.getInstance();
    private final UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();

    /**
     * POST /api/amistades
     * Crea una nueva relación de amistad entre dos usuarios.
     * 
     * Body: { "usuarioId1": 1, "usuarioId2": 2 }
     */
    @POST
    public Response crearAmistad(AmistadRequest request) {
        try {
            if (request == null || request.getUsuarioId1() == null || request.getUsuarioId2() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Se requieren usuarioId1 y usuarioId2"))
                        .build();
            }

            Amistad amistad = amistadRepository.crearAmistad(request.getUsuarioId1(), request.getUsuarioId2());
            return Response.status(Response.Status.CREATED)
                    .entity(amistad)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al crear amistad: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/amistades/usuario/{usuarioId}
     * Obtiene todos los amigos de un usuario (objetos Usuario completos).
     */
    @GET
    @Path("/usuario/{usuarioId}")
    public Response obtenerAmigos(@PathParam("usuarioId") Long usuarioId) {
        try {
            if (!usuarioRepository.buscarPorId(usuarioId).isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Usuario no encontrado con ID: " + usuarioId))
                        .build();
            }

            List<Long> amigosIds = amistadRepository.obtenerAmigosIds(usuarioId);
            List<Usuario> amigos = amigosIds.stream()
                    .map(id -> usuarioRepository.buscarPorId(id))
                    .filter(opt -> opt.isPresent())
                    .map(opt -> opt.get())
                    .collect(Collectors.toList());

            return Response.ok(amigos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener amigos: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/amistades/verificar/{usuarioId1}/{usuarioId2}
     * Verifica si dos usuarios son amigos.
     */
    @GET
    @Path("/verificar/{usuarioId1}/{usuarioId2}")
    public Response verificarAmistad(@PathParam("usuarioId1") Long usuarioId1, 
                                     @PathParam("usuarioId2") Long usuarioId2) {
        try {
            boolean sonAmigos = amistadRepository.sonAmigos(usuarioId1, usuarioId2);
            return Response.ok(new AmistadVerificacionResponse(sonAmigos)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al verificar amistad: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * DELETE /api/amistades/{usuarioId1}/{usuarioId2}
     * Elimina una relación de amistad entre dos usuarios.
     */
    @DELETE
    @Path("/{usuarioId1}/{usuarioId2}")
    public Response eliminarAmistad(@PathParam("usuarioId1") Long usuarioId1, 
                                     @PathParam("usuarioId2") Long usuarioId2) {
        try {
            boolean eliminada = amistadRepository.eliminarAmistad(usuarioId1, usuarioId2);
            if (eliminada) {
                return Response.ok(new MessageResponse("Amistad eliminada correctamente")).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("No se encontró relación de amistad entre los usuarios"))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al eliminar amistad: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/amistades
     * Obtiene todas las relaciones de amistad.
     */
    @GET
    public Response obtenerTodas() {
        try {
            List<Amistad> amistades = amistadRepository.obtenerTodas();
            return Response.ok(amistades).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener amistades: " + e.getMessage()))
                    .build();
        }
    }

    // Clases internas para requests y responses
    public static class AmistadRequest {
        private Long usuarioId1;
        private Long usuarioId2;

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

    public static class AmistadVerificacionResponse {
        private boolean sonAmigos;

        public AmistadVerificacionResponse(boolean sonAmigos) {
            this.sonAmigos = sonAmigos;
        }

        public boolean isSonAmigos() {
            return sonAmigos;
        }
    }
}

