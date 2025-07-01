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
      MatTableModule,
      MatButtonModule,
      MatInputModule,
      MatSelectModule,
      MatDatepickerModule,
      MatNativeDateModule,
      ReactiveFormsModule,
      CommonModule
    ],
    templateUrl: './transactions.component.html',
    styleUrls: ['./transactions.component.css']
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