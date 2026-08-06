package com.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GeneroRequestDTO(
        @NotBlank(message = "O nome do gênero é obrigatório")
        @Size(max = 100, message = "O nome do gênero deve ter no máximo 100 caracteres")
        String nome
) {
}
