import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellerDashboardComponent } from './seller-dashboard.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [SellerDashboardComponent],
  imports: [CommonModule, ReactiveFormsModule],
  exports: [SellerDashboardComponent]
})
export class SellerDashboardModule {}
