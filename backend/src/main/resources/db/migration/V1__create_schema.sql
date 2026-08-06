CREATE TABLE titulo (
    id              BIGSERIAL PRIMARY KEY,
    titulo          VARCHAR(255)    NOT NULL,
    titulo_original VARCHAR(255),
    tipo            VARCHAR(10)     NOT NULL CHECK (tipo IN ('FILME', 'SERIE')),
    sinopse         TEXT,
    ano_lancamento  INTEGER         NOT NULL,
    duracao_minutos INTEGER,
    num_temporadas  INTEGER,
    num_episodios   INTEGER,
    poster_url      VARCHAR(1000),
    created_at      TIMESTAMP       NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT now()
);

CREATE INDEX idx_titulo_titulo ON titulo (titulo);
CREATE INDEX idx_titulo_tipo ON titulo (tipo);
CREATE INDEX idx_titulo_ano ON titulo (ano_lancamento);

CREATE TABLE genero (
    id   BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE titulo_genero (
    titulo_id BIGINT NOT NULL REFERENCES titulo (id) ON DELETE CASCADE,
    genero_id BIGINT NOT NULL REFERENCES genero (id) ON DELETE RESTRICT,
    PRIMARY KEY (titulo_id, genero_id)
);

CREATE TABLE pessoa (
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL,
    foto_url   VARCHAR(1000),
    biografia  TEXT
);

CREATE INDEX idx_pessoa_nome ON pessoa (nome);

CREATE TABLE titulo_pessoa (
    id        BIGSERIAL PRIMARY KEY,
    titulo_id BIGINT      NOT NULL REFERENCES titulo (id) ON DELETE CASCADE,
    pessoa_id BIGINT      NOT NULL REFERENCES pessoa (id) ON DELETE CASCADE,
    papel     VARCHAR(15) NOT NULL CHECK (papel IN ('ATOR', 'DIRETOR', 'ROTEIRISTA')),
    UNIQUE (titulo_id, pessoa_id, papel)
);

CREATE INDEX idx_titulo_pessoa_titulo ON titulo_pessoa (titulo_id);
CREATE INDEX idx_titulo_pessoa_pessoa ON titulo_pessoa (pessoa_id);

CREATE TABLE avaliacao (
    id              BIGSERIAL PRIMARY KEY,
    titulo_id       BIGINT        NOT NULL REFERENCES titulo (id) ON DELETE CASCADE,
    nota            NUMERIC(3, 1) NOT NULL CHECK (nota >= 0 AND nota <= 10),
    comentario      TEXT,
    nome_avaliador  VARCHAR(255)  NOT NULL,
    data_avaliacao  TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE INDEX idx_avaliacao_titulo ON avaliacao (titulo_id);
