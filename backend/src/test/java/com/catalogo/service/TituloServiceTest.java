package com.catalogo.service;

import com.catalogo.dto.TituloRequestDTO;
import com.catalogo.entity.Genero;
import com.catalogo.enums.TipoTitulo;
import com.catalogo.exception.BusinessRuleException;
import com.catalogo.exception.ResourceNotFoundException;
import com.catalogo.mapper.AvaliacaoMapper;
import com.catalogo.mapper.GeneroMapper;
import com.catalogo.mapper.TituloMapper;
import com.catalogo.repository.AvaliacaoRepository;
import com.catalogo.repository.GeneroRepository;
import com.catalogo.repository.PessoaRepository;
import com.catalogo.repository.TituloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TituloServiceTest {

    private TituloService tituloService;
    private GeneroRepository generoRepository;

    @BeforeEach
    void setUp() {
        tituloService = new TituloService();
        tituloService.tituloRepository = mock(TituloRepository.class);
        tituloService.generoRepository = generoRepository = mock(GeneroRepository.class);
        tituloService.pessoaRepository = mock(PessoaRepository.class);
        tituloService.avaliacaoRepository = mock(AvaliacaoRepository.class);
        tituloService.tituloMapper = mock(TituloMapper.class);
        tituloService.generoMapper = mock(GeneroMapper.class);
        tituloService.avaliacaoMapper = mock(AvaliacaoMapper.class);
    }

    private TituloRequestDTO tituloComGeneros(Set<Long> generoIds) {
        return new TituloRequestDTO(
                "Duna", "Dune", TipoTitulo.FILME, "Sinopse", 2021,
                155, null, null, null, generoIds, null);
    }

    @Test
    void deveRejeitarCriacaoDeTituloSemGenero() {
        TituloRequestDTO dto = tituloComGeneros(Set.of());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> tituloService.create(dto));

        assertEquals("É necessário informar ao menos um gênero para o título", exception.getMessage());
    }

    @Test
    void deveLancarNotFoundQuandoGeneroInformadoNaoExiste() {
        TituloRequestDTO dto = tituloComGeneros(Set.of(99L));
        when(generoRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tituloService.create(dto));
    }

    @Test
    void deveCriarTituloQuandoGeneroExiste() {
        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNome("Ficção Científica");

        TituloRequestDTO dto = tituloComGeneros(Set.of(1L));
        when(generoRepository.findByIdOptional(1L)).thenReturn(Optional.of(genero));
        when(tituloService.generoMapper.toDTO(genero)).thenReturn(new com.catalogo.dto.GeneroDTO(1L, "Ficção Científica"));

        var resultado = tituloService.create(dto);

        assertEquals(1, resultado.generos().size());
        assertEquals("Ficção Científica", resultado.generos().get(0).nome());
    }
}
