import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutHomeComponent } from './home/layout-home/layout-home.component';
import { SignupComponent } from './home/signup/signup.component';
import { SigninComponent } from './home/signin/signin.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NotFoundComponent } from './home/not-found/not-found.component';
import { VerifyAccoutComponent } from './home/verify-accout/verify-accout.component';
import { CodeInputModule } from 'angular-code-input';
@NgModule({
  declarations: [
    AppComponent,
    LayoutHomeComponent,
    SignupComponent,
    SigninComponent,
    NotFoundComponent,
    VerifyAccoutComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    CodeInputModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
