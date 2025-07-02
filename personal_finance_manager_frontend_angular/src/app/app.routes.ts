import { Routes } from '@angular/router';
  import { DashboardComponent } from './dashboard/dashboard.component';
  import { TransactionsComponent } from './transactions/transactions.component';
  import { BudgetsComponent } from './budgets/budgets.component';
  import { LoginComponent } from './login/login.component';
  import { RegisterComponent } from './register/register.component';

  export const routes: Routes = [
    { path: '', component: DashboardComponent },
    { path: 'transactions', component: TransactionsComponent },
    { path: 'budgets', component: BudgetsComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent }
  ];