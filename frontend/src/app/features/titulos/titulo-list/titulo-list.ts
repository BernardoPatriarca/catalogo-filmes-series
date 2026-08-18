import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormArray, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Paginator, PaginatorState } from 'primeng/paginator';
import { Button } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { InputText } from 'primeng/inputtext';
import { Textarea } from 'primeng/textarea';
import { InputNumber } from 'primeng/inputnumber';
import { Select } from 'primeng/select';
import { MultiSelect } from 'primeng/multiselect';
import { Tag } from 'primeng/tag';
import { Skeleton } from 'primeng/skeleton';
import { ConfirmationService, MessageService } from 'primeng/api';

import { TituloService } from '../../../core/services/titulo.service';
import { GeneroService } from '../../../core/services/genero.service';
import { PessoaService } from '../../../core/services/pessoa.service';
import { Genero } from '../../../core/models/genero.model';
import { Pessoa } from '../../../core/models/pessoa.model';
import { TituloDetalhe, TituloFiltro, TituloListItem, TituloRequest } from '../../../core/models/titulo.model';
import { PapelPessoa, TipoTitulo } from '../../../core/models/enums';

const TIPO_OPTIONS: { label: string; value: TipoTitulo }[] = [
  { label: 'Filme', value: 'FILME' },
  { label: 'Série', value: 'SERIE' },
];

const PAPEL_OPTIONS: { label: string; value: PapelPessoa }[] = [
  { label: 'Ator', value: 'ATOR' },
  { label: 'Diretor', value: 'DIRETOR' },
  { label: 'Roteirista', value: 'ROTEIRISTA' },
];

const SORT_OPTIONS: { label: string; value: string }[] = [
  { label: 'Título', value: 'titulo' },
  { label: 'Ano', value: 'anoLancamento' },
  { label: 'Nota média', value: 'notaMedia' },
];

@Component({
  selector: 'app-titulo-list',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    Paginator,
    Button,
    Dialog,
    InputText,
    Textarea,
    InputNumber,
    Select,
    MultiSelect,
    Tag,
    Skeleton,
  ],
  templateUrl: './titulo-list.html',
})
export class TituloList implements OnInit {
  private readonly tituloService = inject(TituloService);
  private readonly generoService = inject(GeneroService);
  private readonly pessoaService = inject(PessoaService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  protected readonly tipoOptions = TIPO_OPTIONS;
  protected readonly papelOptions = PAPEL_OPTIONS;
  protected readonly sortOptions = SORT_OPTIONS;

  protected readonly titulos = signal<TituloListItem[]>([]);
  protected readonly totalRecords = signal(0);
  protected readonly loading = signal(false);
  protected readonly generos = signal<Genero[]>([]);
  protected readonly pessoas = signal<Pessoa[]>([]);

  protected readonly dialogVisible = signal(false);
  protected readonly saving = signal(false);
  protected readonly filtersOpen = signal(false);
  protected readonly exporting = signal(false);
  private editingId: number | null = null;

  protected readonly rows = 12;
  protected readonly skeletonItems = Array.from({ length: 12 }, (_, i) => i);
  protected readonly first = signal(0);
  protected readonly sortField = signal('titulo');
  protected readonly sortDirection = signal<'asc' | 'desc'>('asc');

  protected readonly filterForm = this.fb.nonNullable.group({
    titulo: [''],
    generoId: this.fb.control<number | null>(null),
    tipo: this.fb.control<TipoTitulo | null>(null),
    ano: this.fb.control<number | null>(null),
    notaMin: this.fb.control<number | null>(null),
  });

  protected readonly form = this.fb.nonNullable.group({
    titulo: ['', [Validators.required, Validators.maxLength(255)]],
    tituloOriginal: this.fb.control<string | null>(null),
    tipo: this.fb.control<TipoTitulo>('FILME', { nonNullable: true, validators: Validators.required }),
    sinopse: this.fb.control<string | null>(null),
    anoLancamento: this.fb.control<number | null>(null, Validators.required),
    duracaoMinutos: this.fb.control<number | null>(null),
    numTemporadas: this.fb.control<number | null>(null),
    numEpisodios: this.fb.control<number | null>(null),
    posterUrl: this.fb.control<string | null>(null),
    generoIds: this.fb.nonNullable.control<number[]>([], Validators.required),
    elenco: this.fb.array<ReturnType<TituloList['criarElencoGroup']>>([]),
  });

  get elenco(): FormArray {
    return this.form.controls.elenco;
  }

  ngOnInit(): void {
    this.generoService.listAll().subscribe((data) => this.generos.set(data));
    this.pessoaService.search().subscribe((data) => this.pessoas.set(data));

    const generoIdParam = this.route.snapshot.queryParamMap.get('generoId');
    if (generoIdParam) {
      this.filterForm.patchValue({ generoId: Number(generoIdParam) }, { emitEvent: false });
    }

    this.filterForm.valueChanges.subscribe(() => {
      this.first.set(0);
      this.reload();
    });
    this.reload();
  }

  onPageChange(event: PaginatorState): void {
    this.first.set(event.first ?? 0);
    this.reload();
  }

  onSortChange(): void {
    this.first.set(0);
    this.reload();
  }

  toggleSortDirection(): void {
    this.sortDirection.set(this.sortDirection() === 'asc' ? 'desc' : 'asc');
    this.first.set(0);
    this.reload();
  }

  limparFiltros(): void {
    this.filterForm.reset({ titulo: '', generoId: null, tipo: null, ano: null, notaMin: null });
  }

  private filtroAtual(): TituloFiltro {
    const filtros = this.filterForm.getRawValue();
    return {
      titulo: filtros.titulo || undefined,
      generoId: filtros.generoId ?? undefined,
      tipo: filtros.tipo ?? undefined,
      ano: filtros.ano ?? undefined,
      notaMin: filtros.notaMin ?? undefined,
      sort: this.sortField(),
      direction: this.sortDirection(),
    };
  }

  private reload(): void {
    const filtro: TituloFiltro = {
      ...this.filtroAtual(),
      page: Math.floor(this.first() / this.rows),
      size: this.rows,
    };

    this.loading.set(true);
    this.tituloService.search(filtro).subscribe({
      next: (page) => {
        this.titulos.set(page.content);
        this.totalRecords.set(page.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  verDetalhe(titulo: TituloListItem): void {
    this.router.navigate(['/titulos', titulo.id]);
  }

  exportarCsv(): void {
    this.exporting.set(true);
    const filtro: TituloFiltro = { ...this.filtroAtual(), page: 0, size: 1000 };
    this.tituloService.search(filtro).subscribe({
      next: (page) => {
        this.exporting.set(false);
        this.baixarCsv(page.content);
      },
      error: () => this.exporting.set(false),
    });
  }

  private baixarCsv(itens: TituloListItem[]): void {
    const cabecalho = ['Título', 'Tipo', 'Ano', 'Gêneros', 'Nota média', 'Avaliações'];
    const linhas = itens.map((item) => [
      item.titulo,
      item.tipo === 'FILME' ? 'Filme' : 'Série',
      String(item.anoLancamento),
      item.generos.join('; '),
      item.notaMedia !== null ? String(item.notaMedia) : '',
      String(item.totalAvaliacoes),
    ]);

    const escapar = (valor: string) => `"${valor.replace(/"/g, '""')}"`;
    const csv = [cabecalho, ...linhas].map((linha) => linha.map(escapar).join(',')).join('\r\n');

    const blob = new Blob(['﻿' + csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'catalogo-filmes-series.csv';
    link.click();
    URL.revokeObjectURL(url);

    this.messageService.add({ severity: 'success', summary: 'Exportado!', detail: `${itens.length} título(s) baixado(s) em CSV` });
  }

  openNew(): void {
    this.editingId = null;
    this.form.reset({
      titulo: '',
      tituloOriginal: null,
      tipo: 'FILME',
      sinopse: null,
      anoLancamento: null,
      duracaoMinutos: null,
      numTemporadas: null,
      numEpisodios: null,
      posterUrl: null,
      generoIds: [],
    });
    this.elenco.clear();
    this.dialogVisible.set(true);
  }

  openEdit(item: TituloListItem, event?: Event): void {
    event?.stopPropagation();
    this.editingId = item.id;
    this.tituloService.getById(item.id).subscribe((titulo: TituloDetalhe) => {
      this.form.reset({
        titulo: titulo.titulo,
        tituloOriginal: titulo.tituloOriginal,
        tipo: titulo.tipo,
        sinopse: titulo.sinopse,
        anoLancamento: titulo.anoLancamento,
        duracaoMinutos: titulo.duracaoMinutos,
        numTemporadas: titulo.numTemporadas,
        numEpisodios: titulo.numEpisodios,
        posterUrl: titulo.posterUrl,
        generoIds: titulo.generos.map((g) => g.id),
      });
      this.elenco.clear();
      titulo.elenco.forEach((tp) =>
        this.elenco.push(this.criarElencoGroup(tp.pessoaId, tp.papel)),
      );
      this.dialogVisible.set(true);
    });
  }

  private criarElencoGroup(pessoaId: number | null = null, papel: PapelPessoa | null = null) {
    return this.fb.group({
      pessoaId: this.fb.control<number | null>(pessoaId, Validators.required),
      papel: this.fb.control<PapelPessoa | null>(papel, Validators.required),
    });
  }

  addElencoRow(): void {
    this.elenco.push(this.criarElencoGroup());
  }

  removeElencoRow(index: number): void {
    this.elenco.removeAt(index);
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    const raw = this.form.getRawValue();
    const dto: TituloRequest = {
      titulo: raw.titulo,
      tituloOriginal: raw.tituloOriginal,
      tipo: raw.tipo,
      sinopse: raw.sinopse,
      anoLancamento: raw.anoLancamento as number,
      duracaoMinutos: raw.duracaoMinutos,
      numTemporadas: raw.numTemporadas,
      numEpisodios: raw.numEpisodios,
      posterUrl: raw.posterUrl,
      generoIds: raw.generoIds,
      elenco: raw.elenco.map((e: any) => ({ pessoaId: e.pessoaId, papel: e.papel })),
    };

    const request = this.editingId
      ? this.tituloService.update(this.editingId, dto)
      : this.tituloService.create(dto);

    request.subscribe({
      next: () => {
        this.saving.set(false);
        this.dialogVisible.set(false);
        this.messageService.add({
          severity: 'success',
          summary: 'Tudo certo!',
          detail: this.editingId ? 'As alterações foram salvas' : `"${dto.titulo}" já faz parte do catálogo`,
        });
        this.reload();
      },
      error: () => this.saving.set(false),
    });
  }

  confirmDelete(item: TituloListItem, event?: Event): void {
    event?.stopPropagation();
    this.confirmationService.confirm({
      header: 'Remover título',
      message: `Tem certeza que deseja remover "${item.titulo}"? Essa ação não pode ser desfeita.`,
      icon: 'pi pi-exclamation-triangle',
      acceptButtonProps: { severity: 'danger', label: 'Remover' },
      rejectButtonProps: { severity: 'secondary', label: 'Cancelar', outlined: true },
      accept: () => this.delete(item),
    });
  }

  private delete(item: TituloListItem): void {
    this.tituloService.delete(item.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Removido', detail: `"${item.titulo}" saiu do catálogo` });
        this.reload();
      },
    });
  }
}
