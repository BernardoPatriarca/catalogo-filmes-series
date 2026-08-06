import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Menubar } from 'primeng/menubar';
import { Toast } from 'primeng/toast';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, Menubar, Toast, ConfirmDialog],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly menuItems: MenuItem[] = [
    { label: 'Dashboard', icon: 'pi pi-home', routerLink: '/dashboard' },
    { label: 'Filmes & Séries', icon: 'pi pi-video', routerLink: '/titulos' },
    { label: 'Gêneros', icon: 'pi pi-tags', routerLink: '/generos' },
    { label: 'Atores/Diretores', icon: 'pi pi-users', routerLink: '/pessoas' },
  ];
}
