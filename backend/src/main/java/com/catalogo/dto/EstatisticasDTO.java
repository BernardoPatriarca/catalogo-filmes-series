package com.catalogo.dto;

import java.math.BigDecimal;
import java.util.List;

public record EstatisticasDTO(
        long totalFilmes,
        long totalSeries,
        long totalTitulos,
        long totalAvaliacoes,
        BigDecimal mediaGeralNotas,
        String generoMaisAvaliado,
        List<GeneroDistribuicaoDTO> distribuicaoPorGenero,
        List<TituloListItemDTO> top5MelhoresAvaliados
) {
}
