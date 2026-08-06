package com.catalogo.exception;

import com.catalogo.dto.ErrorResponseDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessRuleExceptionMapper implements ExceptionMapper<BusinessRuleException> {

    @Override
    public Response toResponse(BusinessRuleException exception) {
        ErrorResponseDTO body = new ErrorResponseDTO(Response.Status.BAD_REQUEST.getStatusCode(), exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(body).type(MediaType.APPLICATION_JSON).build();
    }
}
