package com.catalogo.mapper;

import com.catalogo.dto.PessoaDTO;
import com.catalogo.dto.PessoaRequestDTO;
import com.catalogo.entity.Pessoa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface PessoaMapper {

    PessoaDTO toDTO(Pessoa pessoa);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participacoes", ignore = true)
    void updateEntityFromDTO(PessoaRequestDTO dto, @MappingTarget Pessoa entity);
}
