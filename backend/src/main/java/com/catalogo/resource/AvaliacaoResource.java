package com.catalogo.resource;

import com.catalogo.dto.AvaliacaoDTO;
import com.catalogo.dto.AvaliacaoRequestDTO;
import com.catalogo.service.AvaliacaoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Avaliações", description = "Avaliações (notas e comentários) de filmes e séries")
public class AvaliacaoResource {

    @Inject
    AvaliacaoService avaliacaoService;

    @GET
    @Path("/api/titulos/{tituloId}/avaliacoes")
    @Operation(summary = "Lista as avaliações de um título")
    public List<AvaliacaoDTO> listByTitulo(@PathParam("tituloId") Long tituloId) {
        return avaliacaoService.listByTitulo(tituloId);
    }

    @POST
    @Path("/api/titulos/{tituloId}/avaliacoes")
    @Operation(summary = "Adiciona uma avaliação a um título")
    public Response create(@PathParam("tituloId") Long tituloId, @Valid AvaliacaoRequestDTO dto) {
        AvaliacaoDTO criada = avaliacaoService.create(tituloId, dto);
        return Response.status(Response.Status.CREATED).entity(criada).build();
    }

    @PUT
    @Path("/api/avaliacoes/{id}")
    @Operation(summary = "Atualiza uma avaliação")
    public AvaliacaoDTO update(@PathParam("id") Long id, @Valid AvaliacaoRequestDTO dto) {
        return avaliacaoService.update(id, dto);
    }

    @DELETE
    @Path("/api/avaliacoes/{id}")
    @Operation(summary = "Remove uma avaliação")
    public Response delete(@PathParam("id") Long id) {
        avaliacaoService.delete(id);
        return Response.noContent().build();
    }
}
