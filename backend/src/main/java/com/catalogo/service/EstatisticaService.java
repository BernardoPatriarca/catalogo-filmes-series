package com.catalogo.service;

import com.catalogo.dto.EstatisticasDTO;
import com.catalogo.dto.GeneroDistribuicaoDTO;
import com.catalogo.dto.TituloListItemDTO;
import com.catalogo.entity.Titulo;
import com.catalogo.enums.TipoTitulo;
import com.catalogo.repository.AvaliacaoRepository;
import com.catalogo.repository.TituloRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class EstatisticaService {

    @Inject
    EntityManager entityManager;

    @Inject
    TituloRepository tituloRepository;

    @Inject
    AvaliacaoRepository avaliacaoRepository;

    public EstatisticasDTO obterEstatisticas() {
        long totalFilmes = tituloRepository.count("tipo", TipoTitulo.FILME);
        long totalSeries = tituloRepository.count("tipo", TipoTitulo.SERIE);
        long totalTitulos = totalFilmes + totalSeries;
        long totalAvaliacoes = avaliacaoRepository.count();

        BigDecimal mediaGeral = entityManager
                .createQuery("SELECT AVG(a.nota) FROM Avaliacao a", Double.class)
                .getResultStream()
                .findFirst()
                .map(v -> v == null ? null : BigDecimal.valueOf(v).setScale(1, RoundingMode.HALF_UP))
                .orElse(null);

        String generoMaisAvaliado = entityManager
                .createQuery(
                        "SELECT g.nome FROM Avaliacao a JOIN a.titulo t JOIN t.generos g " +
                                "GROUP BY g.nome ORDER BY COUNT(a) DESC", String.class)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);

        List<GeneroDistribuicaoDTO> distribuicao = distribuicaoPorGenero();
        List<TituloListItemDTO> top5 = top5MelhoresAvaliados();

        return new EstatisticasDTO(totalFilmes, totalSeries, totalTitulos, totalAvaliacoes, mediaGeral, generoMaisAvaliado, distribuicao, top5);
    }

    @SuppressWarnings("unchecked")
    private List<GeneroDistribuicaoDTO> distribuicaoPorGenero() {
        List<Object[]> rows = entityManager
                .createQuery("SELECT g.nome, COUNT(t) FROM Titulo t JOIN t.generos g GROUP BY g.nome ORDER BY COUNT(t) DESC")
                .getResultList();
        return rows.stream().map(row -> new GeneroDistribuicaoDTO((String) row[0], (Long) row[1])).toList();
    }

    @SuppressWarnings("unchecked")
    private List<TituloListItemDTO> top5MelhoresAvaliados() {
        List<Object[]> rows = entityManager
                .createQuery("SELECT t, AVG(a.nota), COUNT(a) FROM Titulo t JOIN t.avaliacoes a " +
                        "GROUP BY t ORDER BY AVG(a.nota) DESC")
                .setMaxResults(5)
                .getResultList();

        return rows.stream().map(row -> {
            Titulo titulo = (Titulo) row[0];
            Double media = (Double) row[1];
            Long total = (Long) row[2];
            List<String> generos = titulo.getGeneros().stream().map(g -> g.getNome()).sorted().toList();
            return new TituloListItemDTO(
                    titulo.getId(),
                    titulo.getTitulo(),
                    titulo.getTituloOriginal(),
                    titulo.getTipo(),
                    titulo.getAnoLancamento(),
                    titulo.getPosterUrl(),
                    BigDecimal.valueOf(media).setScale(1, RoundingMode.HALF_UP),
                    total,
                    generos
            );
        }).toList();
    }
}
