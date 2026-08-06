package com.catalogo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AvaliacaoDTO(
        Long id,
        Long tituloId,
        BigDecimal nota,
        String comentario,
        String nomeAvaliador,
        LocalDateTime dataAvaliacao
) {
}
