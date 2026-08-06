package com.catalogo.exception;

import com.catalogo.dto.ErrorResponseDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        ErrorResponseDTO body = new ErrorResponseDTO(Response.Status.NOT_FOUND.getStatusCode(), exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(body).type(MediaType.APPLICATION_JSON).build();
    }
}
