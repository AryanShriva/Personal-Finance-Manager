import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TransactionsComponent } from './transactions/transactions.component';
import { BudgetsComponent } from './budgets/budgets.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'transactions', component: TransactionsComponent },
  { path: 'budgets', component: BudgetsComponent }
];