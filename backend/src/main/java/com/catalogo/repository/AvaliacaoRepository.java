package com.catalogo.repository;

import com.catalogo.entity.Avaliacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AvaliacaoRepository implements PanacheRepository<Avaliacao> {

    public List<Avaliacao> findByTituloId(Long tituloId) {
        return list("titulo.id = ?1", Sort.by("dataAvaliacao").descending(), tituloId);
    }

    public BigDecimal notaMediaDoTitulo(Long tituloId) {
        BigDecimal media = getEntityManager()
                .createQuery("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.titulo.id = :tituloId", Double.class)
                .setParameter("tituloId", tituloId)
                .getResultStream()
                .findFirst()
                .map(v -> v == null ? null : BigDecimal.valueOf(v).setScale(1, RoundingMode.HALF_UP))
                .orElse(null);
        return media;
    }

    @SuppressWarnings("unchecked")
    public Map<Long, BigDecimal> notaMediaPorTitulos(List<Long> tituloIds) {
        if (tituloIds == null || tituloIds.isEmpty()) {
            return Map.of();
        }
        List<Object[]> rows = getEntityManager()
                .createQuery("SELECT a.titulo.id, AVG(a.nota) FROM Avaliacao a WHERE a.titulo.id IN :ids GROUP BY a.titulo.id")
                .setParameter("ids", tituloIds)
                .getResultList();
        Map<Long, BigDecimal> resultado = new HashMap<>();
        for (Object[] row : rows) {
            Long tituloId = (Long) row[0];
            Double media = (Double) row[1];
            resultado.put(tituloId, BigDecimal.valueOf(media).setScale(1, RoundingMode.HALF_UP));
        }
        return resultado;
    }

    public long countByTituloId(Long tituloId) {
        return count("titulo.id = ?1", tituloId);
    }

    @SuppressWarnings("unchecked")
    public Map<Long, Long> countPorTitulos(List<Long> tituloIds) {
        if (tituloIds == null || tituloIds.isEmpty()) {
            return Map.of();
        }
        List<Object[]> rows = getEntityManager()
                .createQuery("SELECT a.titulo.id, COUNT(a) FROM Avaliacao a WHERE a.titulo.id IN :ids GROUP BY a.titulo.id")
                .setParameter("ids", tituloIds)
                .getResultList();
        Map<Long, Long> resultado = new HashMap<>();
        for (Object[] row : rows) {
            resultado.put((Long) row[0], (Long) row[1]);
        }
        return resultado;
    }
}
