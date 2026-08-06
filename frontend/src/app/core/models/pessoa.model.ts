export interface Pessoa {
  id: number;
  nome: string;
  fotoUrl: string | null;
  biografia: string | null;
}

export interface PessoaRequest {
  nome: string;
  fotoUrl: string | null;
  biografia: string | null;
}
