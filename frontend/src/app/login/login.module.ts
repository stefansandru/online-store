import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './login.component';
import { RegisterComponent } from './register.component';

@NgModule({
    declarations: [LoginComponent, RegisterComponent],
    imports: [CommonModule, FormsModule],
    exports: [LoginComponent, RegisterComponent],
})
export class LoginModule {}
