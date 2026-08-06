package com.catalogo.service;

import com.catalogo.dto.AvaliacaoDTO;
import com.catalogo.dto.ElencoRequestDTO;
import com.catalogo.dto.GeneroDTO;
import com.catalogo.dto.PageResponseDTO;
import com.catalogo.dto.TituloListItemDTO;
import com.catalogo.dto.TituloPessoaDTO;
import com.catalogo.dto.TituloRequestDTO;
import com.catalogo.dto.TituloResponseDTO;
import com.catalogo.entity.Genero;
import com.catalogo.entity.Pessoa;
import com.catalogo.entity.Titulo;
import com.catalogo.entity.TituloPessoa;
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
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class TituloService {

    @Inject
    TituloRepository tituloRepository;

    @Inject
    GeneroRepository generoRepository;

    @Inject
    PessoaRepository pessoaRepository;

    @Inject
    AvaliacaoRepository avaliacaoRepository;

    @Inject
    TituloMapper tituloMapper;

    @Inject
    GeneroMapper generoMapper;

    @Inject
    AvaliacaoMapper avaliacaoMapper;

    public PageResponseDTO<TituloListItemDTO> search(String titulo, Long generoId, TipoTitulo tipo, Integer ano,
                                                       BigDecimal notaMin, BigDecimal notaMax,
                                                       int page, int size, String sortField, boolean ascending) {
        PanacheQuery<Titulo> query = tituloRepository.search(
                titulo, generoId, tipo, ano, notaMin, notaMax, sortField, ascending, Page.of(page, size));

        List<Titulo> titulos = query.list();
        long totalElements = query.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<Long> ids = titulos.stream().map(Titulo::getId).toList();
        Map<Long, BigDecimal> medias = avaliacaoRepository.notaMediaPorTitulos(ids);
        Map<Long, Long> totais = avaliacaoRepository.countPorTitulos(ids);

        List<TituloListItemDTO> conteudo = titulos.stream()
                .map(t -> toListItemDTO(t, medias.get(t.getId()), totais.getOrDefault(t.getId(), 0L)))
                .toList();

        return new PageResponseDTO<>(conteudo, page, size, totalElements, totalPages);
    }

    public TituloResponseDTO findById(Long id) {
        Titulo titulo = buscarOuFalhar(id);
        BigDecimal media = avaliacaoRepository.notaMediaDoTitulo(id);
        return toResponseDTO(titulo, media);
    }

    @Transactional
    public TituloResponseDTO create(TituloRequestDTO dto) {
        Titulo titulo = new Titulo();
        tituloMapper.updateEntityFromDTO(dto, titulo);
        aplicarGeneros(titulo, dto.generoIds());
        aplicarElenco(titulo, dto.elenco());
        tituloRepository.persist(titulo);
        return toResponseDTO(titulo, null);
    }

    @Transactional
    public TituloResponseDTO update(Long id, TituloRequestDTO dto) {
        Titulo titulo = buscarOuFalhar(id);
        tituloMapper.updateEntityFromDTO(dto, titulo);
        aplicarGeneros(titulo, dto.generoIds());
        aplicarElenco(titulo, dto.elenco());
        BigDecimal media = avaliacaoRepository.notaMediaDoTitulo(id);
        return toResponseDTO(titulo, media);
    }

    @Transactional
    public void delete(Long id) {
        Titulo titulo = buscarOuFalhar(id);
        tituloRepository.delete(titulo);
    }

    private void aplicarGeneros(Titulo titulo, Set<Long> generoIds) {
        if (generoIds == null || generoIds.isEmpty()) {
            throw new BusinessRuleException("É necessário informar ao menos um gênero para o título");
        }
        Set<Genero> generos = new HashSet<>();
        for (Long generoId : generoIds) {
            Genero genero = generoRepository.findByIdOptional(generoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Gênero com id " + generoId + " não encontrado"));
            generos.add(genero);
        }
        titulo.setGeneros(generos);
    }

    private void aplicarElenco(Titulo titulo, List<ElencoRequestDTO> elencoRequest) {
        titulo.getElenco().clear();
        if (elencoRequest == null) {
            return;
        }
        for (ElencoRequestDTO item : elencoRequest) {
            Pessoa pessoa = pessoaRepository.findByIdOptional(item.pessoaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pessoa com id " + item.pessoaId() + " não encontrada"));
            TituloPessoa tituloPessoa = new TituloPessoa();
            tituloPessoa.setTitulo(titulo);
            tituloPessoa.setPessoa(pessoa);
            tituloPessoa.setPapel(item.papel());
            titulo.getElenco().add(tituloPessoa);
        }
    }

    private TituloListItemDTO toListItemDTO(Titulo titulo, BigDecimal notaMedia, long totalAvaliacoes) {
        List<String> generos = titulo.getGeneros().stream().map(Genero::getNome).sorted().toList();
        return new TituloListItemDTO(
                titulo.getId(),
                titulo.getTitulo(),
                titulo.getTituloOriginal(),
                titulo.getTipo(),
                titulo.getAnoLancamento(),
                titulo.getPosterUrl(),
                notaMedia,
                totalAvaliacoes,
                generos
        );
    }

    private TituloResponseDTO toResponseDTO(Titulo titulo, BigDecimal notaMedia) {
        List<GeneroDTO> generos = titulo.getGeneros().stream()
                .map(generoMapper::toDTO)
                .sorted((a, b) -> a.nome().compareToIgnoreCase(b.nome()))
                .toList();

        List<TituloPessoaDTO> elenco = titulo.getElenco().stream()
                .map(tp -> new TituloPessoaDTO(tp.getPessoa().getId(), tp.getPessoa().getNome(), tp.getPessoa().getFotoUrl(), tp.getPapel()))
                .toList();

        List<AvaliacaoDTO> avaliacoes = titulo.getAvaliacoes().stream()
                .map(avaliacaoMapper::toDTO)
                .toList();

        return new TituloResponseDTO(
                titulo.getId(),
                titulo.getTitulo(),
                titulo.getTituloOriginal(),
                titulo.getTipo(),
                titulo.getSinopse(),
                titulo.getAnoLancamento(),
                titulo.getDuracaoMinutos(),
                titulo.getNumTemporadas(),
                titulo.getNumEpisodios(),
                titulo.getPosterUrl(),
                titulo.getCreatedAt(),
                titulo.getUpdatedAt(),
                generos,
                elenco,
                avaliacoes,
                notaMedia,
                avaliacoes.size()
        );
    }

    private Titulo buscarOuFalhar(Long id) {
        return tituloRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Título com id " + id + " não encontrado"));
    }
}
