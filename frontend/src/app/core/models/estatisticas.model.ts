import { TituloListItem } from './titulo.model';

export interface GeneroDistribuicao {
  genero: string;
  totalTitulos: number;
}

export interface Estatisticas {
  totalFilmes: number;
  totalSeries: number;
  totalTitulos: number;
  totalAvaliacoes: number;
  mediaGeralNotas: number | null;
  generoMaisAvaliado: string | null;
  distribuicaoPorGenero: GeneroDistribuicao[];
  top5MelhoresAvaliados: TituloListItem[];
}
