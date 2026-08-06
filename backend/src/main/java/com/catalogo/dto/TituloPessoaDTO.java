package com.catalogo.dto;

import com.catalogo.enums.PapelPessoa;

public record TituloPessoaDTO(Long pessoaId, String nome, String fotoUrl, PapelPessoa papel) {
}
