import { TipoTitulo, PapelPessoa } from './enums';
import { Genero } from './genero.model';
import { Avaliacao } from './avaliacao.model';

export interface TituloListItem {
  id: number;
  titulo: string;
  tituloOriginal: string | null;
  tipo: TipoTitulo;
  anoLancamento: number;
  posterUrl: string | null;
  notaMedia: number | null;
  totalAvaliacoes: number;
  generos: string[];
}

export interface TituloPessoa {
  pessoaId: number;
  nome: string;
  fotoUrl: string | null;
  papel: PapelPessoa;
}

export interface TituloDetalhe {
  id: number;
  titulo: string;
  tituloOriginal: string | null;
  tipo: TipoTitulo;
  sinopse: string | null;
  anoLancamento: number;
  duracaoMinutos: number | null;
  numTemporadas: number | null;
  numEpisodios: number | null;
  posterUrl: string | null;
  createdAt: string;
  updatedAt: string;
  generos: Genero[];
  elenco: TituloPessoa[];
  avaliacoes: Avaliacao[];
  notaMedia: number | null;
  totalAvaliacoes: number;
}

export interface ElencoRequest {
  pessoaId: number;
  papel: PapelPessoa;
}

export interface TituloRequest {
  titulo: string;
  tituloOriginal: string | null;
  tipo: TipoTitulo;
  sinopse: string | null;
  anoLancamento: number;
  duracaoMinutos: number | null;
  numTemporadas: number | null;
  numEpisodios: number | null;
  posterUrl: string | null;
  generoIds: number[];
  elenco: ElencoRequest[];
}

export interface TituloFiltro {
  titulo?: string;
  generoId?: number;
  tipo?: TipoTitulo;
  ano?: number;
  notaMin?: number;
  notaMax?: number;
  page?: number;
  size?: number;
  sort?: string;
  direction?: 'asc' | 'desc';
}
