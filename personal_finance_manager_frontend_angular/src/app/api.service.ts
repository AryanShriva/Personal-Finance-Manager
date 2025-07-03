import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';
  private tokenSubject = new BehaviorSubject<string | null>(localStorage.getItem('token'));
  token$ = this.tokenSubject.asObservable();

  constructor(private http: HttpClient) {}

  setToken(token: string | null) {
    this.tokenSubject.next(token);
    if (token) {
      localStorage.setItem('token', token);
    } else {
      localStorage.removeItem('token');
    }
  }

  getHeaders(): HttpHeaders {
    const token = this.tokenSubject.value;
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  // Transactions
  getTransactions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/transactions/list`, { headers: this.getHeaders() });
  }

  addTransaction(transaction: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/transactions/add`, transaction, { headers: this.getHeaders() });
  }

  // Budgets
  getBudgets(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/budgets/list`, { headers: this.getHeaders() });
  }

  addBudget(budget: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/budgets/add`, budget, { headers: this.getHeaders() });
  }

  getBudgetUsage(budgetId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/budgets/usage/${budgetId}`, { headers: this.getHeaders() });
  }

  isOverspending(budgetId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/budgets/overspending/${budgetId}`, { headers: this.getHeaders() });
  }

  getBudgetSummary(startDate?: string, endDate?: string): Observable<any> {
    let url = `${this.apiUrl}/budgets/summary`;
    if (startDate || endDate) {
      url += '?';
      if (startDate) url += `startDate=${startDate}&`;
      if (endDate) url += `endDate=${endDate}`;
    }
    return this.http.get<any>(url, { headers: this.getHeaders() });
  }

  // Authentication
  register(user: { username: string, password: string, email: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/auth/register`, user).pipe(
      tap(() => this.setToken(null))
    );
  }

  login(credentials: { username: string, password: string }): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap(response => this.setToken(response.token))
    );
  }

  logout() {
    this.setToken(null);
  }
}