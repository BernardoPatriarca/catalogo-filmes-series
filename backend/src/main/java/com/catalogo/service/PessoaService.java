package com.catalogo.service;

import com.catalogo.dto.PessoaDTO;
import com.catalogo.dto.PessoaRequestDTO;
import com.catalogo.entity.Pessoa;
import com.catalogo.exception.ResourceNotFoundException;
import com.catalogo.mapper.PessoaMapper;
import com.catalogo.repository.PessoaRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PessoaService {

    @Inject
    PessoaRepository pessoaRepository;

    @Inject
    PessoaMapper pessoaMapper;

    public List<PessoaDTO> search(String nome) {
        return pessoaRepository.search(nome, Sort.by("nome")).list().stream().map(pessoaMapper::toDTO).toList();
    }

    public PessoaDTO findById(Long id) {
        return pessoaMapper.toDTO(buscarOuFalhar(id));
    }

    @Transactional
    public PessoaDTO create(PessoaRequestDTO dto) {
        Pessoa pessoa = new Pessoa();
        pessoaMapper.updateEntityFromDTO(dto, pessoa);
        pessoaRepository.persist(pessoa);
        return pessoaMapper.toDTO(pessoa);
    }

    @Transactional
    public PessoaDTO update(Long id, PessoaRequestDTO dto) {
        Pessoa pessoa = buscarOuFalhar(id);
        pessoaMapper.updateEntityFromDTO(dto, pessoa);
        return pessoaMapper.toDTO(pessoa);
    }

    @Transactional
    public void delete(Long id) {
        Pessoa pessoa = buscarOuFalhar(id);
        pessoaRepository.delete(pessoa);
    }

    private Pessoa buscarOuFalhar(Long id) {
        return pessoaRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa com id " + id + " não encontrada"));
    }
}
