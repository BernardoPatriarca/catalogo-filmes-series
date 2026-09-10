import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Card } from 'primeng/card';
import { Tag } from 'primeng/tag';
import { UIChart } from 'primeng/chart';
import { Skeleton } from 'primeng/skeleton';
import { Button } from 'primeng/button';
import { EstatisticaService } from '../../core/services/estatistica.service';
import { Estatisticas } from '../../core/models/estatisticas.model';

const CHART_PALETTE = [
  '#ff414d',
  '#fb923c',
  '#fbbf24',
  '#a3e635',
  '#34d399',
  '#22d3ee',
  '#60a5fa',
  '#818cf8',
  '#c084fc',
  '#f472b6',
];

const AXIS_COLOR = '#8f8fa3';
const GRID_COLOR = 'rgba(255, 255, 255, 0.06)';
const TOOLTIP_OPTIONS = {
  backgroundColor: '#1a1a25',
  borderColor: 'rgba(255, 255, 255, 0.12)',
  borderWidth: 1,
  titleColor: '#ececf1',
  bodyColor: '#9d9db0',
  padding: 12,
  cornerRadius: 8,
  displayColors: false,
};

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, Card, Tag, UIChart, Skeleton, Button],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  private readonly estatisticaService = inject(EstatisticaService);

  protected readonly estatisticas = signal<Estatisticas | null>(null);
  protected readonly loading = signal(false);
  protected readonly barChartData = signal<any>(null);
  protected readonly donutChartData = signal<any>(null);

  protected readonly barChartOptions = {
    maintainAspectRatio: false,
    plugins: { legend: { display: false }, tooltip: TOOLTIP_OPTIONS },
    scales: {
      x: { border: { display: false }, grid: { display: false }, ticks: { color: AXIS_COLOR } },
      y: {
        beginAtZero: true,
        border: { display: false },
        ticks: { stepSize: 1, color: AXIS_COLOR },
        grid: { color: GRID_COLOR },
      },
    },
  };

  protected readonly donutChartOptions = {
    maintainAspectRatio: false,
    plugins: {
      legend: { position: 'bottom', labels: { color: AXIS_COLOR, padding: 18, usePointStyle: true, pointStyle: 'circle' } },
      tooltip: TOOLTIP_OPTIONS,
    },
    cutout: '72%',
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
              borderRadius: 8,
              maxBarThickness: 32,
            },
          ],
        });

        this.donutChartData.set({
          labels: ['Filmes', 'Séries'],
          datasets: [
            {
              data: [data.totalFilmes, data.totalSeries],
              backgroundColor: ['#e50914', '#5b63e0'],
              hoverBackgroundColor: ['#ff414d', '#8b93ff'],
              borderColor: '#14141d',
              borderWidth: 3,
              hoverOffset: 6,
            },
          ],
        });

        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}
