import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './user/login/login.component';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './services/auth.guard';
import { RegisterComponent } from './user/register/register.component';
import { CreateComponent } from './bug/create/create.component';
import { DetailsComponent } from './bug/details/details.component';
import { ManagementComponent } from './user/management/management.component';
import { MybugsComponent } from './bug/mybugs/mybugs.component';
import { PublicComponent } from './bug/publicbugs/publicbugs.component';
import { EditComponent } from './user/edit/edit.component';
import { ProfilemanagementComponent } from './user/profilemanagement/profilemanagement.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'create', component: CreateComponent, canActivate: [AuthGuard] },
  { path: 'details/:id', component: DetailsComponent, canActivate: [AuthGuard] },
  { path: 'profiles', component: ManagementComponent, canActivate: [AuthGuard] },
  { path: 'profile/:id', component: EditComponent, canActivate: [AuthGuard] },
  { path: 'myprofile', component: ProfilemanagementComponent, canActivate: [AuthGuard] },
  { path: 'allmybugs', component: MybugsComponent, canActivate: [AuthGuard]},
  { path: 'allpublicbugs', component: PublicComponent, canActivate: [AuthGuard]},
  { path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
