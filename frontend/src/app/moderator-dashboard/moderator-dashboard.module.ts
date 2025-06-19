import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ModeratorDashboardComponent } from './moderator-dashboard.component';

@NgModule({
    declarations: [ModeratorDashboardComponent],
    imports: [CommonModule, FormsModule],
    exports: [ModeratorDashboardComponent],
})
export class ModeratorDashboardModule {}
