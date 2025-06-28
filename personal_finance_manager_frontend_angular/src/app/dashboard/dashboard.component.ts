import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatCardModule],
  template: `
    <mat-card>
      <mat-card-title>Dashboard</mat-card-title>
      <mat-card-content>
        <p>Total Budget Allocated: {{ summary.totalAllocated || 0 }}</p>
        <p>Total Spent: {{ summary.totalSpent || 0 }}</p>
        <p>Remaining: {{ summary.remaining || 0 }}</p>
      </mat-card-content>
    </mat-card>
  `
})
export class DashboardComponent implements OnInit {
  summary: any = {};

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getBudgetSummary('2025-06-01', '2025-06-30').subscribe(data => {
      this.summary = data;
    });
  }
}