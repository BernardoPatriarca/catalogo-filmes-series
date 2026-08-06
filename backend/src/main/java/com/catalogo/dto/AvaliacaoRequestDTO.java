package com.catalogo.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AvaliacaoRequestDTO(
        @NotNull(message = "A nota é obrigatória")
        @DecimalMin(value = "0.0", message = "A nota deve ser no mínimo 0")
        @DecimalMax(value = "10.0", message = "A nota deve ser no máximo 10")
        BigDecimal nota,

        String comentario,

        @NotBlank(message = "O nome do avaliador é obrigatório")
        @Size(max = 255, message = "O nome do avaliador deve ter no máximo 255 caracteres")
        String nomeAvaliador
) {
}
