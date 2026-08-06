package com.catalogo.mapper;

import com.catalogo.dto.GeneroDTO;
import com.catalogo.dto.GeneroRequestDTO;
import com.catalogo.entity.Genero;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface GeneroMapper {

    GeneroDTO toDTO(Genero genero);

    void updateEntityFromDTO(GeneroRequestDTO dto, @MappingTarget Genero entity);
}
