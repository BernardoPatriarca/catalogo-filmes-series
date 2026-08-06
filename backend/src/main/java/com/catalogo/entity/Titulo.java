package com.catalogo.entity;

import com.catalogo.enums.TipoTitulo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "titulo")
public class Titulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Size(max = 255)
    @Column(name = "titulo_original")
    private String tituloOriginal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 10)
    private TipoTitulo tipo;

    @Column(name = "sinopse", columnDefinition = "text")
    private String sinopse;

    @NotNull
    @Column(name = "ano_lancamento", nullable = false)
    private Integer anoLancamento;

    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos;

    @Column(name = "num_temporadas")
    private Integer numTemporadas;

    @Column(name = "num_episodios")
    private Integer numEpisodios;

    @Size(max = 1000)
    @Column(name = "poster_url", length = 1000)
    private String posterUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "titulo_genero",
            joinColumns = @JoinColumn(name = "titulo_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Genero> generos = new HashSet<>();

    @OneToMany(mappedBy = "titulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TituloPessoa> elenco = new ArrayList<>();

    @OneToMany(mappedBy = "titulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTituloOriginal() {
        return tituloOriginal;
    }

    public void setTituloOriginal(String tituloOriginal) {
        this.tituloOriginal = tituloOriginal;
    }

    public TipoTitulo getTipo() {
        return tipo;
    }

    public void setTipo(TipoTitulo tipo) {
        this.tipo = tipo;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Integer getNumTemporadas() {
        return numTemporadas;
    }

    public void setNumTemporadas(Integer numTemporadas) {
        this.numTemporadas = numTemporadas;
    }

    public Integer getNumEpisodios() {
        return numEpisodios;
    }

    public void setNumEpisodios(Integer numEpisodios) {
        this.numEpisodios = numEpisodios;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<Genero> generos) {
        this.generos = generos;
    }

    public List<TituloPessoa> getElenco() {
        return elenco;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }
}
