package com.catalogo.service;

import com.catalogo.dto.GeneroRequestDTO;
import com.catalogo.entity.Genero;
import com.catalogo.exception.BusinessRuleException;
import com.catalogo.exception.ResourceNotFoundException;
import com.catalogo.mapper.GeneroMapper;
import com.catalogo.repository.GeneroRepository;
import com.catalogo.repository.TituloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeneroServiceTest {

    private GeneroService generoService;
    private GeneroRepository generoRepository;

    @BeforeEach
    void setUp() {
        generoService = new GeneroService();
        generoService.generoRepository = generoRepository = mock(GeneroRepository.class);
        generoService.tituloRepository = mock(TituloRepository.class);
        generoService.generoMapper = mock(GeneroMapper.class);
    }

    @Test
    void deveRejeitarGeneroDuplicado() {
        Genero existente = new Genero();
        existente.setId(1L);
        existente.setNome("Drama");
        when(generoRepository.findByNomeIgnoreCase("Drama")).thenReturn(Optional.of(existente));

        assertThrows(BusinessRuleException.class, () -> generoService.create(new GeneroRequestDTO("Drama")));
    }

    @Test
    void deveLancarNotFoundAoBuscarGeneroInexistente() {
        when(generoRepository.findByIdOptional(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> generoService.findById(404L));
    }
}
