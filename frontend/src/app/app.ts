import { Component, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { Drawer } from 'primeng/drawer';
import { Toast } from 'primeng/toast';
import { ConfirmDialog } from 'primeng/confirmdialog';

interface NavLink {
  label: string;
  icon: string;
  route: string;
  hint: string;
}

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, Drawer, Toast, ConfirmDialog],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly navLinks: NavLink[] = [
    { label: 'Dashboard', icon: 'pi pi-chart-pie', route: '/dashboard', hint: 'Visão geral' },
    { label: 'Filmes & Séries', icon: 'pi pi-video', route: '/titulos', hint: 'Catálogo' },
    { label: 'Gêneros', icon: 'pi pi-tags', route: '/generos', hint: 'Categorias' },
    { label: 'Elenco', icon: 'pi pi-users', route: '/pessoas', hint: 'Pessoas' },
  ];

  protected readonly mobileNavOpen = signal(false);

  constructor(router: Router) {
    router.events.pipe(filter((e) => e instanceof NavigationEnd)).subscribe(() => this.mobileNavOpen.set(false));
  }
}
