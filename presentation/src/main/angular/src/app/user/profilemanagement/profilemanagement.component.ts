import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/objects/user';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'wt2-profilemanagement',
  templateUrl: './profilemanagement.component.html',
  styleUrls: ['./profilemanagement.component.sass']
})
export class ProfilemanagementComponent implements OnInit {

  public user: User = null;
  public id: number = -1;

  public passwordError: boolean = false;

  firstname: string;
  lastname: string;
  email: string;
  password: string = "";
  validate_password: string = "";

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private sessionService: SessionService) { }

  ngOnInit(): void {
    this.user = this.sessionService.user;
    this.firstname = this.user.firstname,
    this.lastname = this.user.lastname,
    this.email = this.user.email
  }

  updateUser() {
    let user = new User;
    user.id = this.user.id;

    var hasChanges: boolean = false;

    this.firstname = this.firstname.trim();
    this.lastname = this.lastname.trim();
    this.email = this.email.trim();

    if(this.firstname != null && this.firstname.length > 0
        && this.firstname != this.user.firstname){
      user.firstname = this.firstname;
      hasChanges = true;
    }
    if(this.lastname != null && this.lastname.length > 0
        && this.lastname != this.user.lastname){
      user.lastname = this.lastname;
      hasChanges = true;
    }
    if(this.email != null && this.email.length > 0
        && this.email != this.user.email){
      user.email = this.email;
      hasChanges = true;
    }

    this.password = this.password.trim();
    this.validate_password = this.validate_password.trim();
    if(this.password.length > 0 || this.validate_password.length > 0){
      if(this.password == this.validate_password){
        user.password = this.password;
        hasChanges = true;
      }else{
        this.passwordError = true;
        return;
      }
    }

    if(hasChanges){
      this.userService.update(user).subscribe(
        user => this.user = user,
        console.error
      );
    }
  }

}
