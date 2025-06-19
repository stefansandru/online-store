import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SellerDashboardComponent } from './seller-dashboard/seller-dashboard.component';
import { BuyerDashboardComponent } from './buyer-dashboard/buyer-dashboard.component';
import { ModeratorDashboardComponent } from './moderator-dashboard/moderator-dashboard.component';
import { roleGuard } from './role.guard';
import { RegisterComponent } from './login/register.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    {
        path: 'seller',
        component: SellerDashboardComponent,
        canActivate: [roleGuard('SELLER')],
    },
    {
        path: 'buyer',
        component: BuyerDashboardComponent,
        canActivate: [roleGuard('BUYER')],
    },
    {
        path: 'moderator',
        component: ModeratorDashboardComponent,
        canActivate: [roleGuard('MODERATOR')],
    },
    { path: 'register', component: RegisterComponent },
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: '**', redirectTo: '/login' },
];
