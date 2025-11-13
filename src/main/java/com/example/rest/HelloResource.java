package com.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Ejemplo de recurso REST que demuestra diferentes operaciones HTTP.
 */
@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloResource {

    /**
     * GET /api/hello
     * Retorna un mensaje de saludo simple.
     */
    @GET
    public Response hello() {
        return Response.ok()
                .entity(new MessageResponse("Hola desde JAX-RS!"))
                .build();
    }

    /**
     * GET /api/hello/{name}
     * Retorna un saludo personalizado con el nombre proporcionado.
     */
    @GET
    @Path("/{name}")
    public Response helloWithName(@PathParam("name") String name) {
        return Response.ok()
                .entity(new MessageResponse("Hola, " + name + "!"))
                .build();
    }

    /**
     * POST /api/hello
     * Recibe un mensaje y lo retorna con un saludo.
     */
    @POST
    public Response createMessage(MessageRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("El mensaje no puede estar vacío"))
                    .build();
        }
        
        return Response.status(Response.Status.CREATED)
                .entity(new MessageResponse("Mensaje recibido: " + request.getMessage()))
                .build();
    }

    /**
     * PUT /api/hello/{id}
     * Actualiza un mensaje por ID.
     */
    @PUT
    @Path("/{id}")
    public Response updateMessage(@PathParam("id") String id, MessageRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("El mensaje no puede estar vacío"))
                    .build();
        }
        
        return Response.ok()
                .entity(new MessageResponse("Mensaje " + id + " actualizado: " + request.getMessage()))
                .build();
    }

    /**
     * DELETE /api/hello/{id}
     * Elimina un mensaje por ID.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteMessage(@PathParam("id") String id) {
        return Response.ok()
                .entity(new MessageResponse("Mensaje " + id + " eliminado"))
                .build();
    }

    /**
     * Clase interna para representar una respuesta de mensaje.
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
     * Clase interna para representar una solicitud de mensaje.
     */
    public static class MessageRequest {
        private String message;

        public MessageRequest() {
        }

        public MessageRequest(String message) {
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
     * Clase interna para representar una respuesta de error.
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
}

