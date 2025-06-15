import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  role = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        // Redirect based on role
        const role = this.authService.getRole();
        if (role === 'SELLER') this.router.navigate(['/seller']);
        else if (role === 'BUYER') this.router.navigate(['/buyer']);
        else if (role === 'MODERATOR') this.router.navigate(['/moderator']);
        else this.router.navigate(['/login']);
      },
      error: () => {
        this.error = 'Invalid credentials';
      }
    });
  }

  onRegister() {
    this.router.navigate(['/register']);
  }
}
