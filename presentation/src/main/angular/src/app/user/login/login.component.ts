import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Bug } from '../../objects/bug';
import { SessionService } from '../../services/session.service';
import { first, take, last } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'wt2-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

  public loading = false;

  public username: string = "";
  public password: string = "";
  public errorMessage: string;

  public bugs: Bug[] = [];

  constructor(private sessionService: SessionService,
              private router: Router) {
    // redirect to home if already logged in
    if (this.sessionService.loggedIn) {
      this.router.navigate(['/']);
    }
  }

  /*
  login(e: Event) {
    e.preventDefault();
    this.loading = true;
    this.errorMessage = null;
    if (this.username != "" && this.password != "") {
      this.sessionService.login(this.username, this.password).pipe(last()).subscribe(
        data => {
            this.router.navigate(['/home']);
        },
        error => {
            this.errorMessage = "Failed to login";
            this.loading = false;
        }
      );
    }
    this.loading = false;
  }
  */

  login(e: Event) {
    e.preventDefault();
    this.loading = true;
    this.errorMessage = null;
    this.errorMessage = null;
    if (this.username != "" && this.password != "") {
      this.sessionService.login(this.username, this.password).subscribe(
        () => {
                this.sessionService.getUser();
        },
        () => {
                this.errorMessage = "Failed to login";
                this.loading = false;
        }
      );
    }
    this.loading = false;
  }

  ngOnInit(): void {
  }

}
