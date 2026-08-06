package com.catalogo.dto;

import com.catalogo.enums.PapelPessoa;
import jakarta.validation.constraints.NotNull;

public record ElencoRequestDTO(
        @NotNull(message = "O id da pessoa é obrigatório") Long pessoaId,
        @NotNull(message = "O papel é obrigatório") PapelPessoa papel
) {
}
