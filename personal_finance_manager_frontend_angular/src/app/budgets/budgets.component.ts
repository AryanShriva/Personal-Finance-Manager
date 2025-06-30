import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-budgets',
  standalone: true,
  imports: [
    MatTableModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule, // Added for formGroup
    CommonModule // Added for date pipe
  ],
  templateUrl: './budgets.component.html',
  styleUrls: ['./budgets.component.css']
})
export class BudgetsComponent implements OnInit {
  budgets: any[] = [];
  displayedColumns: string[] = ['amount', 'startDate', 'endDate', 'category', 'usage', 'overspending'];
  showForm = false;
  budgetForm: FormGroup;
  usageMap: { [key: number]: number } = {};
  overspendingMap: { [key: number]: boolean } = {};

  constructor(private apiService: ApiService, private fb: FormBuilder) {
    this.budgetForm = this.fb.group({
      amount: ['', [Validators.required, Validators.min(0)]],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      category: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.loadBudgets();
  }

  loadBudgets() {
    this.apiService.getBudgets().subscribe(data => {
      this.budgets = data;
      this.budgets.forEach(budget => {
        this.apiService.getBudgetUsage(budget.id).subscribe(usage => {
          this.usageMap[budget.id] = usage;
        });
        this.apiService.isOverspending(budget.id).subscribe(overspending => {
          this.overspendingMap[budget.id] = overspending;
        });
      });
    });
  }

  openForm() {
    this.showForm = true;
  }

  cancelForm() {
    this.showForm = false;
    this.budgetForm.reset();
  }

  onSubmit() {
    if (this.budgetForm.valid) {
      const budget = this.budgetForm.value;
      this.apiService.addBudget(budget).subscribe(() => {
        this.loadBudgets();
        this.cancelForm();
      });
    }
  }
}