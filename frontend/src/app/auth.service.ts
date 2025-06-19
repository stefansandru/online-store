import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private tokenKey = 'jwt_token';
    private roleKey = 'user_role';
    private apiUrl = 'http://localhost:8080/api/auth/login';
    private registerUrl = 'http://localhost:8080/api/auth/register';

    constructor(
        private http: HttpClient,
        private router: Router,
    ) {}

    login(username: string, password: string): Observable<any> {
        return this.http.post<any>(this.apiUrl, { username, password }).pipe(
            tap((res) => {
                if (res.token) {
                    sessionStorage.setItem(this.tokenKey, res.token);
                    sessionStorage.setItem(
                        this.roleKey,
                        this.getRoleFromToken(res.token),
                    );
                }
            }),
        );
    }

    logout() {
        sessionStorage.removeItem(this.tokenKey);
        sessionStorage.removeItem(this.roleKey);
        this.router.navigate(['/login']);
    }

    register(
        username: string,
        password: string,
        role: string,
    ): Observable<any> {
        return this.http
            .post<any>(this.registerUrl, { username, password, role })
            .pipe(
                tap((res) => {
                    if (res.token) {
                        sessionStorage.setItem(this.tokenKey, res.token);
                        sessionStorage.setItem(
                            this.roleKey,
                            this.getRoleFromToken(res.token),
                        );
                    }
                }),
            );
    }

    getToken(): string | null {
        return sessionStorage.getItem(this.tokenKey);
    }

    getRole(): string | null {
        return sessionStorage.getItem(this.roleKey);
    }

    isLoggedIn(): boolean {
        return !!this.getToken();
    }

    private getRoleFromToken(token: string): string {
        try {
            const payload = token.split('.')[1];
            const decoded = JSON.parse(atob(payload));
            if (decoded.role) {
                return decoded.role;
            } else if (decoded.roles && Array.isArray(decoded.roles)) {
                return decoded.roles[0];
            } else if (
                decoded.authorities &&
                Array.isArray(decoded.authorities)
            ) {
                return decoded.authorities[0];
            }
            return '';
        } catch (e) {
            return '';
        }
    }
}
