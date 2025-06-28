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
  selector: 'app-transactions',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule
  ],
  template: `
    <h2>Transactions</h2>
    <button mat-raised-button color="primary" (click)="openForm()">Add Transaction</button>

    <div *ngIf="showForm">
      <form [formGroup]="transactionForm" (ngSubmit)="onSubmit()">
        <mat-form-field>
          <input matInput placeholder="Amount" formControlName="amount" type="number" required>
        </mat-form-field>
        <mat-form-field>
          <mat-select placeholder="Type" formControlName="type" required>
            <mat-option value="INCOME">Income</mat-option>
            <mat-option value="EXPENSE">Expense</mat-option>
          </mat-select>
        </mat-form-field>
        <mat-form-field>
          <input matInput [matDatepicker]="picker" placeholder="Date" formControlName="date" required>
          <mat-datepicker-toggle matIconSuffix [for]="picker"></mat-datepicker-toggle>
          <mat-datepicker #picker></mat-datepicker>
        </mat-form-field>
        <mat-form-field>
          <input matInput placeholder="Category" formControlName="category" required>
        </mat-form-field>
        <button mat-raised-button color="primary" type="submit" [disabled]="transactionForm.invalid">Save</button>
        <button mat-button type="button" (click)="cancelForm()">Cancel</button>
      </form>
    </div>

    <table mat-table [dataSource]="transactions" class="mat-elevation-z8">
      <ng-container matColumnDef="amount">
        <th mat-header-cell *matHeaderCellDef> Amount </th>
        <td mat-cell *matCellDef="let element"> {{element.amount}} </td>
      </ng-container>
      <ng-container matColumnDef="type">
        <th mat-header-cell *matHeaderCellDef> Type </th>
        <td mat-cell *matCellDef="let element"> {{element.type}} </td>
      </ng-container>
      <ng-container matColumnDef="date">
        <th mat-header-cell *matHeaderCellDef> Date </th>
        <td mat-cell *matCellDef="let element"> {{element.date | date}} </td>
      </ng-container>
      <ng-container matColumnDef="category">
        <th mat-header-cell *matHeaderCellDef> Category </th>
        <td mat-cell *matCellDef="let element"> {{element.category}} </td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
  `,
  styles: [`
    table {
      width: 100%;
      margin-top: 20px;
    }
    .mat-form-field {
      width: 200px;
      margin: 10px;
    }
    form {
      display: flex;
      flex-wrap: wrap;
      margin-top: 20px;
    }
  `]
})
export class TransactionsComponent implements OnInit {
  transactions: any[] = [];
  displayedColumns: string[] = ['amount', 'type', 'date', 'category'];
  showForm = false;
  transactionForm: FormGroup;

  constructor(private apiService: ApiService, private fb: FormBuilder) {
    this.transactionForm = this.fb.group({
      amount: ['', [Validators.required, Validators.min(0)]],
      type: ['', Validators.required],
      date: ['', Validators.required],
      category: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.loadTransactions();
  }

  loadTransactions() {
    this.apiService.getTransactions().subscribe(data => {
      this.transactions = data;
    });
  }

  openForm() {
    this.showForm = true;
  }

  cancelForm() {
    this.showForm = false;
    this.transactionForm.reset();
  }

  onSubmit() {
    if (this.transactionForm.valid) {
      const transaction = this.transactionForm.value;
      this.apiService.addTransaction(transaction).subscribe(() => {
        this.loadTransactions();
        this.cancelForm();
      });
    }
  }
}