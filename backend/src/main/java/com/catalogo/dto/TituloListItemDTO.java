package com.catalogo.dto;

import com.catalogo.enums.TipoTitulo;

import java.math.BigDecimal;
import java.util.List;

public record TituloListItemDTO(
        Long id,
        String titulo,
        String tituloOriginal,
        TipoTitulo tipo,
        Integer anoLancamento,
        String posterUrl,
        BigDecimal notaMedia,
        long totalAvaliacoes,
        List<String> generos
) {
}
