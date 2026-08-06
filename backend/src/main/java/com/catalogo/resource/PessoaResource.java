package com.catalogo.resource;

import com.catalogo.dto.PessoaDTO;
import com.catalogo.dto.PessoaRequestDTO;
import com.catalogo.service.PessoaService;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/pessoas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Atores/Diretores", description = "CRUD de pessoas (atores, diretores, roteiristas)")
public class PessoaResource {

    @Inject
    PessoaService pessoaService;

    @GET
    @Operation(summary = "Lista/busca pessoas por nome")
    public List<PessoaDTO> search(@QueryParam("nome") String nome) {
        return pessoaService.search(nome);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Busca uma pessoa por id")
    public PessoaDTO findById(@PathParam("id") Long id) {
        return pessoaService.findById(id);
    }

    @POST
    @Operation(summary = "Cria uma nova pessoa")
    public Response create(@Valid PessoaRequestDTO dto) {
        PessoaDTO criada = pessoaService.create(dto);
        return Response.status(Response.Status.CREATED).entity(criada).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualiza uma pessoa existente")
    public PessoaDTO update(@PathParam("id") Long id, @Valid PessoaRequestDTO dto) {
        return pessoaService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remove uma pessoa")
    public Response delete(@PathParam("id") Long id) {
        pessoaService.delete(id);
        return Response.noContent().build();
    }
}
