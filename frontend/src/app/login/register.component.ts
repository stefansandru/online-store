import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
    username = '';
    password = '';
    role = '';
    message = '';

    constructor(
        private authService: AuthService,
        private router: Router,
    ) {}

    onRegister() {
        this.authService
            .register(this.username, this.password, this.role)
            .subscribe({
                next: () => {
                    const role = this.authService.getRole();
                    if (role === 'SELLER') this.router.navigate(['/seller']);
                    else if (role === 'BUYER') this.router.navigate(['/buyer']);
                    else if (role === 'MODERATOR')
                        this.router.navigate(['/moderator']);
                    else this.router.navigate(['/login']);
                },
                error: () => {
                    this.message = 'Username already exists';
                },
            });
    }
}
