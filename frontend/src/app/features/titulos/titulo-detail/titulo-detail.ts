import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Button } from 'primeng/button';
import { Tag } from 'primeng/tag';
import { Rating } from 'primeng/rating';
import { InputText } from 'primeng/inputtext';
import { Textarea } from 'primeng/textarea';
import { Card } from 'primeng/card';
import { ConfirmationService, MessageService } from 'primeng/api';

import { TituloService } from '../../../core/services/titulo.service';
import { AvaliacaoService } from '../../../core/services/avaliacao.service';
import { TituloDetalhe, TituloPessoa } from '../../../core/models/titulo.model';
import { AvaliacaoRequest } from '../../../core/models/avaliacao.model';
import { PapelPessoa } from '../../../core/models/enums';

@Component({
  selector: 'app-titulo-detail',
  imports: [ReactiveFormsModule, RouterLink, DatePipe, Button, Tag, Rating, InputText, Textarea, Card],
  templateUrl: './titulo-detail.html',
})
export class TituloDetail implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly tituloService = inject(TituloService);
  private readonly avaliacaoService = inject(AvaliacaoService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);
  private readonly fb = inject(FormBuilder);

  protected readonly titulo = signal<TituloDetalhe | null>(null);
  protected readonly loading = signal(false);
  protected readonly submitting = signal(false);

  protected readonly avaliacaoForm = this.fb.nonNullable.group({
    nomeAvaliador: ['', Validators.required],
    nota: this.fb.control<number | null>(null, Validators.required),
    comentario: this.fb.control<string | null>(null),
  });

  ngOnInit(): void {
    this.load();
  }

  private tituloId(): number {
    return Number(this.route.snapshot.paramMap.get('id'));
  }

  load(): void {
    this.loading.set(true);
    this.tituloService.getById(this.tituloId()).subscribe({
      next: (data) => {
        this.titulo.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  elencoPorPapel(elenco: TituloPessoa[], papel: PapelPessoa): TituloPessoa[] {
    return elenco.filter((e) => e.papel === papel);
  }

  enviarAvaliacao(): void {
    if (this.avaliacaoForm.invalid) {
      this.avaliacaoForm.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    const raw = this.avaliacaoForm.getRawValue();
    const dto: AvaliacaoRequest = { ...raw, nota: raw.nota as number };
    this.avaliacaoService.create(this.tituloId(), dto).subscribe({
      next: () => {
        this.submitting.set(false);
        this.avaliacaoForm.reset({ nomeAvaliador: '', nota: null, comentario: null });
        this.messageService.add({ severity: 'success', summary: 'Obrigado!', detail: 'Sua avaliação foi enviada' });
        this.load();
      },
      error: () => this.submitting.set(false),
    });
  }

  confirmDeleteAvaliacao(avaliacaoId: number): void {
    this.confirmationService.confirm({
      header: 'Remover avaliação',
      message: 'Tem certeza que deseja remover esta avaliação? Essa ação não pode ser desfeita.',
      icon: 'pi pi-exclamation-triangle',
      acceptButtonProps: { severity: 'danger', label: 'Remover' },
      rejectButtonProps: { severity: 'secondary', label: 'Cancelar', outlined: true },
      accept: () => {
        this.avaliacaoService.delete(avaliacaoId).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Removida', detail: 'A avaliação foi excluída' });
            this.load();
          },
        });
      },
    });
  }

  confirmDeleteTitulo(): void {
    const titulo = this.titulo();
    if (!titulo) return;

    this.confirmationService.confirm({
      header: 'Remover título',
      message: `Tem certeza que deseja remover "${titulo.titulo}"? Essa ação não pode ser desfeita.`,
      icon: 'pi pi-exclamation-triangle',
      acceptButtonProps: { severity: 'danger', label: 'Remover' },
      rejectButtonProps: { severity: 'secondary', label: 'Cancelar', outlined: true },
      accept: () => {
        this.tituloService.delete(titulo.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Removido', detail: `"${titulo.titulo}" saiu do catálogo` });
            this.router.navigate(['/titulos']);
          },
        });
      },
    });
  }
}
