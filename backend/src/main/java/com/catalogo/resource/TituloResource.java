package com.catalogo.resource;

import com.catalogo.dto.PageResponseDTO;
import com.catalogo.dto.TituloListItemDTO;
import com.catalogo.dto.TituloRequestDTO;
import com.catalogo.dto.TituloResponseDTO;
import com.catalogo.enums.TipoTitulo;
import com.catalogo.service.TituloService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.math.BigDecimal;

@Path("/api/titulos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Títulos", description = "CRUD e busca de filmes e séries")
public class TituloResource {

    @Inject
    TituloService tituloService;

    @GET
    @Operation(summary = "Lista/busca títulos com filtros, paginação e ordenação")
    public PageResponseDTO<TituloListItemDTO> search(
            @QueryParam("titulo") String titulo,
            @QueryParam("generoId") Long generoId,
            @QueryParam("tipo") TipoTitulo tipo,
            @QueryParam("ano") Integer ano,
            @QueryParam("notaMin") BigDecimal notaMin,
            @QueryParam("notaMax") BigDecimal notaMax,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sort") @DefaultValue("titulo") String sort,
            @QueryParam("direction") @DefaultValue("asc") String direction) {
        boolean ascending = !"desc".equalsIgnoreCase(direction);
        return tituloService.search(titulo, generoId, tipo, ano, notaMin, notaMax, page, size, sort, ascending);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Busca um título por id, com elenco, gêneros e avaliações")
    public TituloResponseDTO findById(@PathParam("id") Long id) {
        return tituloService.findById(id);
    }

    @POST
    @Operation(summary = "Cria um novo filme ou série")
    public Response create(@Valid TituloRequestDTO dto) {
        TituloResponseDTO criado = tituloService.create(dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualiza um filme ou série existente")
    public TituloResponseDTO update(@PathParam("id") Long id, @Valid TituloRequestDTO dto) {
        return tituloService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remove um filme ou série")
    public Response delete(@PathParam("id") Long id) {
        tituloService.delete(id);
        return Response.noContent().build();
    }
}
