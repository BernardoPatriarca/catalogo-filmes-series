package com.catalogo.resource;

import com.catalogo.dto.EstatisticasDTO;
import com.catalogo.service.EstatisticaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/estatisticas")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Estatísticas", description = "Estatísticas gerais do catálogo")
public class EstatisticaResource {

    @Inject
    EstatisticaService estatisticaService;

    @GET
    @Operation(summary = "Retorna estatísticas gerais: totais, gênero mais avaliado, top 5 melhores avaliados")
    public EstatisticasDTO obterEstatisticas() {
        return estatisticaService.obterEstatisticas();
    }
}
