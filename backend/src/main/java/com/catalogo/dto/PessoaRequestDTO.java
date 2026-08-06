package com.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PessoaRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String nome,

        @Size(max = 1000, message = "A URL da foto deve ter no máximo 1000 caracteres")
        String fotoUrl,

        String biografia
) {
}
