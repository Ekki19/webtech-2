import { Component, OnInit, Input } from '@angular/core';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import { User } from 'src/app/objects/user';

@Component({
  selector: 'wt2-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.sass']
})
export class MenuComponent implements OnInit {

  @Input()
  public showCreateBugButton: boolean;

  navbarOpen = false;
  public user: User = null;
  constructor(private sessionService: SessionService,
              private router: Router) { }

  ngOnInit(): void { }

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }

  public getInitials(): string {
    this.user = this.sessionService.user;
    let user = this.sessionService.user;
    return user.firstname.substring(0,1).toUpperCase() + user.lastname.substring(0,1).toUpperCase();
  }

  isAdmin(): boolean {
    let role = this.user.role;
    return role === "admin";
  }

  isAllowedToCreateBug() {
    let role = this.user.role;
    return role === "admin" || role === "moderator";
  }

  logout() {
    this.sessionService.logout().subscribe();
    this.router.navigate(['/login']);
  }

}
