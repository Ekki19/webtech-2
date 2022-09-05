import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { LoginComponent } from './user/login/login.component';
import { SessionService } from './services/session.service';
import { HomeComponent } from './home/home.component';
import { MenuComponent } from './components/menu/menu.component';
import { BugListComponent } from './components/bug-list/bug-list.component';
import { RegisterComponent } from './user/register/register.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { CreateComponent } from './bug/create/create.component';
import { DetailsComponent } from './bug/details/details.component';
import { ConfirmationComponent } from './components/confirmation/confirmation.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { ManagementComponent } from './user/management/management.component';
import { PublicComponent } from './bug/publicbugs/publicbugs.component';
import { MybugsComponent } from './bug/mybugs/mybugs.component';
import { EditComponent } from './user/edit/edit.component';
import { ProfilemanagementComponent } from './user/profilemanagement/profilemanagement.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    MenuComponent,
    BugListComponent,
    RegisterComponent,
    CreateComponent,
    DetailsComponent,
    ConfirmationComponent,
    UserListComponent,
    ManagementComponent,
    PublicComponent,
    MybugsComponent,
    EditComponent,
    ProfilemanagementComponent
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatMenuModule,
    MatButtonModule
  ],
  providers: [SessionService],
  bootstrap: [AppComponent]
})
export class AppModule { }
