package com.catalogo.mapper;

import com.catalogo.dto.TituloRequestDTO;
import com.catalogo.entity.Titulo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface TituloMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "generos", ignore = true)
    @Mapping(target = "elenco", ignore = true)
    @Mapping(target = "avaliacoes", ignore = true)
    void updateEntityFromDTO(TituloRequestDTO dto, @MappingTarget Titulo entity);
}
