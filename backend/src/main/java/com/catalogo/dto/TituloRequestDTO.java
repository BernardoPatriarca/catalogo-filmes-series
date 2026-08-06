package com.catalogo.dto;

import com.catalogo.enums.TipoTitulo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record TituloRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
        String titulo,

        String tituloOriginal,

        @NotNull(message = "O tipo (FILME ou SERIE) é obrigatório")
        TipoTitulo tipo,

        String sinopse,

        @NotNull(message = "O ano de lançamento é obrigatório")
        Integer anoLancamento,

        Integer duracaoMinutos,
        Integer numTemporadas,
        Integer numEpisodios,

        @Size(max = 1000, message = "A URL do pôster deve ter no máximo 1000 caracteres")
        String posterUrl,

        @NotEmpty(message = "É necessário informar ao menos um gênero")
        Set<Long> generoIds,

        @Valid
        List<ElencoRequestDTO> elenco
) {
}
