package com.catalogo.exception;

import com.catalogo.dto.ErrorResponseDTO;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GenericExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException webEx) {
            Response response = webEx.getResponse();
            ErrorResponseDTO body = new ErrorResponseDTO(response.getStatus(), webEx.getMessage());
            return Response.fromResponse(response).entity(body).type(MediaType.APPLICATION_JSON).build();
        }

        LOG.error("Erro inesperado ao processar requisição", exception);
        ErrorResponseDTO body = new ErrorResponseDTO(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Erro interno no servidor"
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(body).type(MediaType.APPLICATION_JSON).build();
    }
}
