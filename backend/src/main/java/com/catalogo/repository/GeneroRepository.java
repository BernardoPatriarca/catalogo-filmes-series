package com.catalogo.repository;

import com.catalogo.entity.Genero;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class GeneroRepository implements PanacheRepository<Genero> {

    public Optional<Genero> findByNomeIgnoreCase(String nome) {
        return find("LOWER(nome) = LOWER(?1)", nome).firstResultOptional();
    }
}
