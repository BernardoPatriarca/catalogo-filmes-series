package com.catalogo.repository;

import com.catalogo.entity.Titulo;
import com.catalogo.enums.TipoTitulo;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class TituloRepository implements PanacheRepository<Titulo> {

    private static final Set<String> CAMPOS_ORDENACAO_VALIDOS = Set.of("titulo", "anoLancamento", "notaMedia");

    public PanacheQuery<Titulo> search(String titulo, Long generoId, TipoTitulo tipo, Integer ano,
                                        BigDecimal notaMin, BigDecimal notaMax, Long pessoaId, Long excluirId,
                                        String sortField, boolean ascending, Page page) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT t FROM Titulo t LEFT JOIN t.generos g WHERE 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (titulo != null && !titulo.isBlank()) {
            jpql.append(" AND LOWER(t.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))");
            params.put("titulo", titulo);
        }
        if (generoId != null) {
            jpql.append(" AND g.id = :generoId");
            params.put("generoId", generoId);
        }
        if (tipo != null) {
            jpql.append(" AND t.tipo = :tipo");
            params.put("tipo", tipo);
        }
        if (ano != null) {
            jpql.append(" AND t.anoLancamento = :ano");
            params.put("ano", ano);
        }
        if (notaMin != null) {
            jpql.append(" AND COALESCE((SELECT AVG(a.nota) FROM Avaliacao a WHERE a.titulo = t), 0) >= :notaMin");
            params.put("notaMin", notaMin);
        }
        if (notaMax != null) {
            jpql.append(" AND COALESCE((SELECT AVG(a.nota) FROM Avaliacao a WHERE a.titulo = t), 0) <= :notaMax");
            params.put("notaMax", notaMax);
        }
        if (pessoaId != null) {
            jpql.append(" AND EXISTS (SELECT 1 FROM TituloPessoa tp WHERE tp.titulo = t AND tp.pessoa.id = :pessoaId)");
            params.put("pessoaId", pessoaId);
        }
        if (excluirId != null) {
            jpql.append(" AND t.id <> :excluirId");
            params.put("excluirId", excluirId);
        }

        String campo = CAMPOS_ORDENACAO_VALIDOS.contains(sortField) ? sortField : "titulo";
        String direcao = ascending ? "ASC" : "DESC";
        if ("notaMedia".equals(campo)) {
            jpql.append(" ORDER BY COALESCE((SELECT AVG(a.nota) FROM Avaliacao a WHERE a.titulo = t), 0) ").append(direcao);
        } else {
            jpql.append(" ORDER BY t.").append(campo).append(" ").append(direcao);
        }

        PanacheQuery<Titulo> query = find(jpql.toString(), params);
        return page != null ? query.page(page) : query;
    }

    public long countByGeneroId(Long generoId) {
        return getEntityManager()
                .createQuery("SELECT COUNT(t) FROM Titulo t JOIN t.generos g WHERE g.id = :generoId", Long.class)
                .setParameter("generoId", generoId)
                .getSingleResult();
    }
}
