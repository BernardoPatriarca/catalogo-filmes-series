INSERT INTO genero (nome) VALUES
    ('Ação'), ('Ficção Científica'), ('Drama'), ('Comédia'), ('Suspense'), ('Fantasia'), ('Crime'), ('Animação');

INSERT INTO pessoa (nome, foto_url, biografia) VALUES
    ('Christopher Nolan', NULL, 'Diretor e roteirista britânico-americano conhecido por narrativas não lineares.'),
    ('Cillian Murphy', NULL, 'Ator irlandês.'),
    ('Vince Gilligan', NULL, 'Roteirista e produtor de televisão americano.'),
    ('Bryan Cranston', NULL, 'Ator americano.'),
    ('Hayao Miyazaki', NULL, 'Diretor de animação japonês, cofundador do Studio Ghibli.');

INSERT INTO titulo (titulo, titulo_original, tipo, sinopse, ano_lancamento, duracao_minutos, poster_url) VALUES
    ('Oppenheimer', 'Oppenheimer', 'FILME', 'A história do físico J. Robert Oppenheimer e o desenvolvimento da bomba atômica.', 2023, 180,
     'https://upload.wikimedia.org/wikipedia/en/4/4a/Oppenheimer_%28film%29.jpg');

INSERT INTO titulo (titulo, titulo_original, tipo, sinopse, ano_lancamento, num_temporadas, num_episodios, poster_url) VALUES
    ('Breaking Bad', 'Breaking Bad', 'SERIE', 'Um professor de química se torna fabricante de metanfetamina.', 2008, 5, 62,
     'https://upload.wikimedia.org/wikipedia/en/6/61/Breaking_Bad_title_card.png');

INSERT INTO titulo (titulo, titulo_original, tipo, sinopse, ano_lancamento, duracao_minutos, poster_url) VALUES
    ('A Viagem de Chihiro', 'Sen to Chihiro no Kamikakushi', 'FILME', 'Uma menina se aventura em um mundo mágico habitado por espíritos.', 2001, 125,
     'https://upload.wikimedia.org/wikipedia/en/e/e8/Spirited_Away_Japanese_poster.png');

INSERT INTO titulo_genero (titulo_id, genero_id)
SELECT t.id, g.id FROM titulo t, genero g WHERE t.titulo = 'Oppenheimer' AND g.nome IN ('Drama');

INSERT INTO titulo_genero (titulo_id, genero_id)
SELECT t.id, g.id FROM titulo t, genero g WHERE t.titulo = 'Breaking Bad' AND g.nome IN ('Drama', 'Crime', 'Suspense');

INSERT INTO titulo_genero (titulo_id, genero_id)
SELECT t.id, g.id FROM titulo t, genero g WHERE t.titulo = 'A Viagem de Chihiro' AND g.nome IN ('Fantasia', 'Animação');

INSERT INTO titulo_pessoa (titulo_id, pessoa_id, papel)
SELECT t.id, p.id, 'DIRETOR' FROM titulo t, pessoa p WHERE t.titulo = 'Oppenheimer' AND p.nome = 'Christopher Nolan';

INSERT INTO titulo_pessoa (titulo_id, pessoa_id, papel)
SELECT t.id, p.id, 'ATOR' FROM titulo t, pessoa p WHERE t.titulo = 'Oppenheimer' AND p.nome = 'Cillian Murphy';

INSERT INTO titulo_pessoa (titulo_id, pessoa_id, papel)
SELECT t.id, p.id, 'ROTEIRISTA' FROM titulo t, pessoa p WHERE t.titulo = 'Breaking Bad' AND p.nome = 'Vince Gilligan';

INSERT INTO titulo_pessoa (titulo_id, pessoa_id, papel)
SELECT t.id, p.id, 'ATOR' FROM titulo t, pessoa p WHERE t.titulo = 'Breaking Bad' AND p.nome = 'Bryan Cranston';

INSERT INTO titulo_pessoa (titulo_id, pessoa_id, papel)
SELECT t.id, p.id, 'DIRETOR' FROM titulo t, pessoa p WHERE t.titulo = 'A Viagem de Chihiro' AND p.nome = 'Hayao Miyazaki';

INSERT INTO avaliacao (titulo_id, nota, comentario, nome_avaliador)
SELECT id, 9.5, 'Atuações incríveis e trilha sonora impactante.', 'Marina' FROM titulo WHERE titulo = 'Oppenheimer';

INSERT INTO avaliacao (titulo_id, nota, comentario, nome_avaliador)
SELECT id, 8.0, 'Muito longo, mas vale a pena.', 'Rafael' FROM titulo WHERE titulo = 'Oppenheimer';

INSERT INTO avaliacao (titulo_id, nota, comentario, nome_avaliador)
SELECT id, 10.0, 'Melhor série já feita.', 'Marina' FROM titulo WHERE titulo = 'Breaking Bad';

INSERT INTO avaliacao (titulo_id, nota, comentario, nome_avaliador)
SELECT id, 9.8, 'Evolução de personagem impecável.', 'Lucas' FROM titulo WHERE titulo = 'Breaking Bad';

INSERT INTO avaliacao (titulo_id, nota, comentario, nome_avaliador)
SELECT id, 9.0, 'Um clássico atemporal do Studio Ghibli.', 'Beatriz' FROM titulo WHERE titulo = 'A Viagem de Chihiro';
