import { Injectable } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';

export function roleGuard(expectedRole: string): CanActivateFn {
    return () => {
        const authService = inject(AuthService);
        const router = inject(Router);
        const role = authService.getRole();
        if (authService.isLoggedIn() && role === expectedRole) {
            return true;
        }
        router.navigate(['/login']);
        return false;
    };
}
