import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';
  private headers = new HttpHeaders({
    'Authorization': 'Bearer temp-jwt-bypass-testuser3' // Replace with dynamic token later
  });

  constructor(private http: HttpClient) {}

  getTransactions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/transactions/list`, { headers: this.headers });
  }

  addTransaction(transaction: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/transactions/add`, transaction, { headers: this.headers });
  }

  getBudgets(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/budgets/list`, { headers: this.headers });
  }

  addBudget(budget: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/budgets/add`, budget, { headers: this.headers });
  }

  getBudgetUsage(budgetId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/budgets/usage/${budgetId}`, { headers: this.headers });
  }

  isOverspending(budgetId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/budgets/overspending/${budgetId}`, { headers: this.headers });
  }

  getBudgetSummary(startDate?: string, endDate?: string): Observable<any> {
    let url = `${this.apiUrl}/budgets/summary`;
    if (startDate || endDate) {
      url += '?';
      if (startDate) url += `startDate=${startDate}&`;
      if (endDate) url += `endDate=${endDate}`;
    }
    return this.http.get<any>(url, { headers: this.headers });
  }
}