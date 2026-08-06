package com.catalogo.repository;

import com.catalogo.entity.Pessoa;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PessoaRepository implements PanacheRepository<Pessoa> {

    public PanacheQuery<Pessoa> search(String nome, Sort sort) {
        if (nome != null && !nome.isBlank()) {
            return find("LOWER(nome) LIKE LOWER(CONCAT('%', ?1, '%'))", sort, nome);
        }
        return findAll(sort);
    }
}
