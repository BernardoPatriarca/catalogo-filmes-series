package com.catalogo.mapper;

import com.catalogo.dto.AvaliacaoDTO;
import com.catalogo.dto.AvaliacaoRequestDTO;
import com.catalogo.entity.Avaliacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AvaliacaoMapper {

    @Mapping(target = "tituloId", source = "titulo.id")
    AvaliacaoDTO toDTO(Avaliacao avaliacao);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "titulo", ignore = true)
    @Mapping(target = "dataAvaliacao", ignore = true)
    void updateEntityFromDTO(AvaliacaoRequestDTO dto, @MappingTarget Avaliacao entity);
}
