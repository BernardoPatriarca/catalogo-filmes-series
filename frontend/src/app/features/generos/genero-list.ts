import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Button } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { InputText } from 'primeng/inputtext';
import { Skeleton } from 'primeng/skeleton';
import { ConfirmationService, MessageService } from 'primeng/api';
import { GeneroService } from '../../core/services/genero.service';
import { Genero } from '../../core/models/genero.model';

@Component({
  selector: 'app-genero-list',
  imports: [ReactiveFormsModule, RouterLink, Button, Dialog, InputText, Skeleton],
  templateUrl: './genero-list.html',
})
export class GeneroList implements OnInit {
  private readonly generoService = inject(GeneroService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);
  private readonly fb = inject(FormBuilder);

  protected readonly skeletonItems = Array.from({ length: 12 }, (_, i) => i);
  protected readonly generos = signal<Genero[]>([]);
  protected readonly loading = signal(false);
  protected readonly dialogVisible = signal(false);
  protected readonly saving = signal(false);
  private editingId: number | null = null;

  protected readonly form = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.maxLength(100)]],
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.generoService.listAll().subscribe({
      next: (data) => {
        this.generos.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  openNew(): void {
    this.editingId = null;
    this.form.reset({ nome: '' });
    this.dialogVisible.set(true);
  }

  openEdit(genero: Genero, event?: Event): void {
    event?.stopPropagation();
    event?.preventDefault();
    this.editingId = genero.id;
    this.form.reset({ nome: genero.nome });
    this.dialogVisible.set(true);
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    const dto = this.form.getRawValue();
    const request = this.editingId
      ? this.generoService.update(this.editingId, dto)
      : this.generoService.create(dto);

    request.subscribe({
      next: () => {
        this.saving.set(false);
        this.dialogVisible.set(false);
        this.messageService.add({
          severity: 'success',
          summary: 'Tudo certo!',
          detail: this.editingId ? 'As alterações foram salvas' : `"${dto.nome}" já está na lista de gêneros`,
        });
        this.load();
      },
      error: () => this.saving.set(false),
    });
  }

  confirmDelete(genero: Genero, event?: Event): void {
    event?.stopPropagation();
    event?.preventDefault();
    this.confirmationService.confirm({
      header: 'Remover gênero',
      message: `Tem certeza que deseja remover o gênero "${genero.nome}"? Essa ação não pode ser desfeita.`,
      icon: 'pi pi-exclamation-triangle',
      acceptButtonProps: { severity: 'danger', label: 'Remover' },
      rejectButtonProps: { severity: 'secondary', label: 'Cancelar', outlined: true },
      accept: () => this.delete(genero),
    });
  }

  private delete(genero: Genero): void {
    this.generoService.delete(genero.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Removido', detail: `"${genero.nome}" saiu da lista` });
        this.load();
      },
    });
  }
}
