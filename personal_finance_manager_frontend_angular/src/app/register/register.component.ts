import { Component } from '@angular/core';
  import { ApiService } from '../api.service';
  import { MatInputModule } from '@angular/material/input';
  import { MatButtonModule } from '@angular/material/button';
  import { FormBuilder, FormGroup, Validators } from '@angular/forms';
  import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [MatInputModule, MatButtonModule, ReactiveFormsModule],
    templateUrl: './register.component.html'
  })
  export class RegisterComponent {
    registerForm: FormGroup;

    constructor(private fb: FormBuilder, private apiService: ApiService, private router: Router) {
      this.registerForm = this.fb.group({
        username: ['', Validators.required],
        password: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]]
      });
    }

    onSubmit() {
      if (this.registerForm.valid) {
        this.apiService.register(this.registerForm.value).subscribe(() => {
          this.router.navigate(['/login']);
        });
      }
    }
  }