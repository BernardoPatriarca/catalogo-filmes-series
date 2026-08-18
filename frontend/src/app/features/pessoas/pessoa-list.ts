import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Button } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { InputText } from 'primeng/inputtext';
import { Textarea } from 'primeng/textarea';
import { Skeleton } from 'primeng/skeleton';
import { ConfirmationService, MessageService } from 'primeng/api';
import { PessoaService } from '../../core/services/pessoa.service';
import { Pessoa } from '../../core/models/pessoa.model';

@Component({
  selector: 'app-pessoa-list',
  imports: [ReactiveFormsModule, RouterLink, Button, Dialog, InputText, Textarea, Skeleton],
  templateUrl: './pessoa-list.html',
})
export class PessoaList implements OnInit {
  private readonly pessoaService = inject(PessoaService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);
  private readonly fb = inject(FormBuilder);

  protected readonly skeletonItems = Array.from({ length: 12 }, (_, i) => i);
  protected readonly pessoas = signal<Pessoa[]>([]);
  protected readonly loading = signal(false);
  protected readonly dialogVisible = signal(false);
  protected readonly saving = signal(false);
  private editingId: number | null = null;

  protected readonly form = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.maxLength(255)]],
    fotoUrl: this.fb.control<string | null>(null),
    biografia: this.fb.control<string | null>(null),
  });

  ngOnInit(): void {
    this.load();
  }

  load(nome?: string): void {
    this.loading.set(true);
    this.pessoaService.search(nome).subscribe({
      next: (data) => {
        this.pessoas.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  openNew(): void {
    this.editingId = null;
    this.form.reset({ nome: '', fotoUrl: null, biografia: null });
    this.dialogVisible.set(true);
  }

  openEdit(pessoa: Pessoa, event?: Event): void {
    event?.stopPropagation();
    event?.preventDefault();
    this.editingId = pessoa.id;
    this.form.reset({ nome: pessoa.nome, fotoUrl: pessoa.fotoUrl, biografia: pessoa.biografia });
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
      ? this.pessoaService.update(this.editingId, dto)
      : this.pessoaService.create(dto);

    request.subscribe({
      next: () => {
        this.saving.set(false);
        this.dialogVisible.set(false);
        this.messageService.add({
          severity: 'success',
          summary: 'Tudo certo!',
          detail: this.editingId ? 'As alterações foram salvas' : `"${dto.nome}" já faz parte do elenco`,
        });
        this.load();
      },
      error: () => this.saving.set(false),
    });
  }

  confirmDelete(pessoa: Pessoa, event?: Event): void {
    event?.stopPropagation();
    event?.preventDefault();
    this.confirmationService.confirm({
      header: 'Remover pessoa',
      message: `Tem certeza que deseja remover "${pessoa.nome}"? Essa ação não pode ser desfeita.`,
      icon: 'pi pi-exclamation-triangle',
      acceptButtonProps: { severity: 'danger', label: 'Remover' },
      rejectButtonProps: { severity: 'secondary', label: 'Cancelar', outlined: true },
      accept: () => this.delete(pessoa),
    });
  }

  private delete(pessoa: Pessoa): void {
    this.pessoaService.delete(pessoa.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Removida', detail: `"${pessoa.nome}" saiu da lista` });
        this.load();
      },
    });
  }
}
