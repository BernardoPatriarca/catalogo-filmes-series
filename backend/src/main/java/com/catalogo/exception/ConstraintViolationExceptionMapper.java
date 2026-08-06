package com.catalogo.exception;

import com.catalogo.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations().stream()
                .map(this::formatViolation)
                .toList();
        ErrorResponseDTO body = new ErrorResponseDTO(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Erro de validação",
                errors
        );
        return Response.status(Response.Status.BAD_REQUEST).entity(body).type(MediaType.APPLICATION_JSON).build();
    }

    private String formatViolation(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        return (path.isEmpty() ? "" : path + ": ") + violation.getMessage();
    }
}
