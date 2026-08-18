import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PessoaService } from '../../../core/services/pessoa.service';
import { TituloService } from '../../../core/services/titulo.service';
import { Pessoa } from '../../../core/models/pessoa.model';
import { TituloListItem } from '../../../core/models/titulo.model';

@Component({
  selector: 'app-pessoa-detail',
  imports: [RouterLink],
  templateUrl: './pessoa-detail.html',
})
export class PessoaDetail implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly pessoaService = inject(PessoaService);
  private readonly tituloService = inject(TituloService);

  protected readonly pessoa = signal<Pessoa | null>(null);
  protected readonly titulos = signal<TituloListItem[]>([]);
  protected readonly loading = signal(false);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loading.set(true);
    this.pessoaService.getById(id).subscribe({
      next: (data) => this.pessoa.set(data),
    });
    this.tituloService.search({ pessoaId: id, size: 48, sort: 'anoLancamento', direction: 'desc' }).subscribe({
      next: (page) => {
        this.titulos.set(page.content);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  verDetalhe(titulo: TituloListItem): void {
    this.router.navigate(['/titulos', titulo.id]);
  }
}
