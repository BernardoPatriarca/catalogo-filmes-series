package com.catalogo.resource;

import com.catalogo.dto.GeneroDTO;
import com.catalogo.dto.GeneroRequestDTO;
import com.catalogo.service.GeneroService;
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

@Path("/api/generos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Gêneros", description = "CRUD de gêneros")
public class GeneroResource {

    @Inject
    GeneroService generoService;

    @GET
    @Operation(summary = "Lista todos os gêneros")
    public List<GeneroDTO> listAll() {
        return generoService.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Busca um gênero por id")
    public GeneroDTO findById(@PathParam("id") Long id) {
        return generoService.findById(id);
    }

    @POST
    @Operation(summary = "Cria um novo gênero")
    public Response create(@Valid GeneroRequestDTO dto) {
        GeneroDTO criado = generoService.create(dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualiza um gênero existente")
    public GeneroDTO update(@PathParam("id") Long id, @Valid GeneroRequestDTO dto) {
        return generoService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remove um gênero")
    public Response delete(@PathParam("id") Long id) {
        generoService.delete(id);
        return Response.noContent().build();
    }
}
