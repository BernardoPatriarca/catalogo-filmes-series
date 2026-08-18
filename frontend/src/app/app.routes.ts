import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/dashboard/dashboard').then((m) => m.Dashboard),
  },
  {
    path: 'titulos',
    loadComponent: () => import('./features/titulos/titulo-list/titulo-list').then((m) => m.TituloList),
  },
  {
    path: 'titulos/:id',
    loadComponent: () => import('./features/titulos/titulo-detail/titulo-detail').then((m) => m.TituloDetail),
  },
  {
    path: 'generos',
    loadComponent: () => import('./features/generos/genero-list').then((m) => m.GeneroList),
  },
  {
    path: 'pessoas',
    loadComponent: () => import('./features/pessoas/pessoa-list').then((m) => m.PessoaList),
  },
  {
    path: 'pessoas/:id',
    loadComponent: () => import('./features/pessoas/pessoa-detail/pessoa-detail').then((m) => m.PessoaDetail),
  },
  { path: '**', redirectTo: 'dashboard' },
];
