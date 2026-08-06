import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Card } from 'primeng/card';
import { Tag } from 'primeng/tag';
import { UIChart } from 'primeng/chart';
import { EstatisticaService } from '../../core/services/estatistica.service';
import { Estatisticas } from '../../core/models/estatisticas.model';

const CHART_PALETTE = [
  '#ef4444',
  '#f97316',
  '#f59e0b',
  '#eab308',
  '#84cc16',
  '#10b981',
  '#06b6d4',
  '#3b82f6',
  '#8b5cf6',
  '#d946ef',
  '#ec4899',
  '#f43f5e',
];

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, Card, Tag, UIChart],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  private readonly estatisticaService = inject(EstatisticaService);

  protected readonly estatisticas = signal<Estatisticas | null>(null);
  protected readonly loading = signal(false);
  protected readonly barChartData = signal<any>(null);
  protected readonly donutChartData = signal<any>(null);

  protected readonly barChartOptions = {
    plugins: { legend: { display: false } },
    scales: {
      x: { grid: { display: false }, ticks: { color: '#71717a' } },
      y: { beginAtZero: true, ticks: { stepSize: 1, color: '#71717a' }, grid: { color: 'rgba(24,24,27,0.06)' } },
    },
  };

  protected readonly donutChartOptions = {
    plugins: { legend: { position: 'bottom', labels: { color: '#3f3f46', padding: 16 } } },
    cutout: '68%',
  };

  ngOnInit(): void {
    this.loading.set(true);
    this.estatisticaService.obter().subscribe({
      next: (data) => {
        this.estatisticas.set(data);

        const barColors = data.distribuicaoPorGenero.map((_, i) => CHART_PALETTE[i % CHART_PALETTE.length]);

        this.barChartData.set({
          labels: data.distribuicaoPorGenero.map((g) => g.genero),
          datasets: [
            {
              label: 'Títulos por gênero',
              data: data.distribuicaoPorGenero.map((g) => g.totalTitulos),
              backgroundColor: barColors,
              hoverBackgroundColor: barColors,
              borderRadius: 6,
              maxBarThickness: 34,
            },
          ],
        });

        this.donutChartData.set({
          labels: ['Filmes', 'Séries'],
          datasets: [
            {
              data: [data.totalFilmes, data.totalSeries],
              backgroundColor: ['#ef4444', '#6366f1'],
              hoverBackgroundColor: ['#f87171', '#818cf8'],
              borderWidth: 0,
            },
          ],
        });

        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}
