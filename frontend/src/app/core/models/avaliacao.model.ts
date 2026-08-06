export interface Avaliacao {
  id: number;
  tituloId: number;
  nota: number;
  comentario: string | null;
  nomeAvaliador: string;
  dataAvaliacao: string;
}

export interface AvaliacaoRequest {
  nota: number;
  comentario: string | null;
  nomeAvaliador: string;
}
