package com.catalogo.service;

import com.catalogo.dto.AvaliacaoDTO;
import com.catalogo.dto.AvaliacaoRequestDTO;
import com.catalogo.entity.Avaliacao;
import com.catalogo.entity.Titulo;
import com.catalogo.exception.ResourceNotFoundException;
import com.catalogo.mapper.AvaliacaoMapper;
import com.catalogo.repository.AvaliacaoRepository;
import com.catalogo.repository.TituloRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class AvaliacaoService {

    @Inject
    AvaliacaoRepository avaliacaoRepository;

    @Inject
    TituloRepository tituloRepository;

    @Inject
    AvaliacaoMapper avaliacaoMapper;

    public List<AvaliacaoDTO> listByTitulo(Long tituloId) {
        buscarTituloOuFalhar(tituloId);
        return avaliacaoRepository.findByTituloId(tituloId).stream().map(avaliacaoMapper::toDTO).toList();
    }

    @Transactional
    public AvaliacaoDTO create(Long tituloId, AvaliacaoRequestDTO dto) {
        Titulo titulo = buscarTituloOuFalhar(tituloId);
        Avaliacao avaliacao = new Avaliacao();
        avaliacaoMapper.updateEntityFromDTO(dto, avaliacao);
        avaliacao.setTitulo(titulo);
        avaliacaoRepository.persist(avaliacao);
        return avaliacaoMapper.toDTO(avaliacao);
    }

    @Transactional
    public AvaliacaoDTO update(Long avaliacaoId, AvaliacaoRequestDTO dto) {
        Avaliacao avaliacao = buscarAvaliacaoOuFalhar(avaliacaoId);
        avaliacaoMapper.updateEntityFromDTO(dto, avaliacao);
        return avaliacaoMapper.toDTO(avaliacao);
    }

    @Transactional
    public void delete(Long avaliacaoId) {
        Avaliacao avaliacao = buscarAvaliacaoOuFalhar(avaliacaoId);
        avaliacaoRepository.delete(avaliacao);
    }

    private Titulo buscarTituloOuFalhar(Long tituloId) {
        return tituloRepository.findByIdOptional(tituloId)
                .orElseThrow(() -> new ResourceNotFoundException("Título com id " + tituloId + " não encontrado"));
    }

    private Avaliacao buscarAvaliacaoOuFalhar(Long avaliacaoId) {
        return avaliacaoRepository.findByIdOptional(avaliacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação com id " + avaliacaoId + " não encontrada"));
    }
}
