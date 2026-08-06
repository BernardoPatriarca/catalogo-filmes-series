package com.catalogo.dto;

import com.catalogo.enums.TipoTitulo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TituloResponseDTO(
        Long id,
        String titulo,
        String tituloOriginal,
        TipoTitulo tipo,
        String sinopse,
        Integer anoLancamento,
        Integer duracaoMinutos,
        Integer numTemporadas,
        Integer numEpisodios,
        String posterUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<GeneroDTO> generos,
        List<TituloPessoaDTO> elenco,
        List<AvaliacaoDTO> avaliacoes,
        BigDecimal notaMedia,
        long totalAvaliacoes
) {
}
