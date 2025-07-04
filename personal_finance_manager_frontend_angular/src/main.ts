import { provideHttpClient } from '@angular/common/http';
  import { bootstrapApplication } from '@angular/platform-browser';
  import { AppComponent } from './app/app.component';
  import { provideAnimations } from '@angular/platform-browser/animations';
  import { provideRouter } from '@angular/router';
  import { routes } from './app/app.routes';

  bootstrapApplication(AppComponent, {
    providers: [
      provideHttpClient(), // Added to provide HttpClient
      provideAnimations(),
      provideRouter(routes)
    ],
  }).catch(err => console.error(err));