package com.catalogo.service;

import com.catalogo.dto.GeneroDTO;
import com.catalogo.dto.GeneroRequestDTO;
import com.catalogo.entity.Genero;
import com.catalogo.exception.BusinessRuleException;
import com.catalogo.exception.ResourceNotFoundException;
import com.catalogo.mapper.GeneroMapper;
import com.catalogo.repository.GeneroRepository;
import com.catalogo.repository.TituloRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class GeneroService {

    @Inject
    GeneroRepository generoRepository;

    @Inject
    TituloRepository tituloRepository;

    @Inject
    GeneroMapper generoMapper;

    public List<GeneroDTO> listAll() {
        return generoRepository.listAll(Sort.by("nome")).stream().map(generoMapper::toDTO).toList();
    }

    public GeneroDTO findById(Long id) {
        return generoMapper.toDTO(buscarOuFalhar(id));
    }

    @Transactional
    public GeneroDTO create(GeneroRequestDTO dto) {
        generoRepository.findByNomeIgnoreCase(dto.nome()).ifPresent(g -> {
            throw new BusinessRuleException("Já existe um gênero com o nome '" + dto.nome() + "'");
        });
        Genero genero = new Genero();
        generoMapper.updateEntityFromDTO(dto, genero);
        generoRepository.persist(genero);
        return generoMapper.toDTO(genero);
    }

    @Transactional
    public GeneroDTO update(Long id, GeneroRequestDTO dto) {
        Genero genero = buscarOuFalhar(id);
        generoRepository.findByNomeIgnoreCase(dto.nome())
                .filter(g -> !g.getId().equals(id))
                .ifPresent(g -> {
                    throw new BusinessRuleException("Já existe um gênero com o nome '" + dto.nome() + "'");
                });
        generoMapper.updateEntityFromDTO(dto, genero);
        return generoMapper.toDTO(genero);
    }

    @Transactional
    public void delete(Long id) {
        Genero genero = buscarOuFalhar(id);
        long titulosVinculados = tituloRepository.countByGeneroId(id);
        if (titulosVinculados > 0) {
            throw new BusinessRuleException(
                    "Não é possível remover o gênero '" + genero.getNome() + "' pois está associado a " + titulosVinculados + " título(s)");
        }
        generoRepository.delete(genero);
    }

    private Genero buscarOuFalhar(Long id) {
        return generoRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gênero com id " + id + " não encontrado"));
    }
}
