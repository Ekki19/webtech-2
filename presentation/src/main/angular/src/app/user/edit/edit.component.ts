import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/objects/user';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'wt2-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.sass']
})
export class EditComponent implements OnInit {

  public user: User = null;
  public id: number = -1;

  public passwordError: boolean = false;

  username: string;
  firstname: string;
  lastname: string;
  email: string;
  role: string;
  password: string = "";
  validate_password: string = "";

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private sessionService: SessionService) { }

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id');
    this.userService.getById(this.id).subscribe(
      user => {
        this.user = user,
        this.username = this.user.username,
        this.firstname = this.user.firstname,
        this.lastname = this.user.lastname,
        this.email = this.user.email,
        this.role = this.user.role
    },
      console.error
    );
  }

  updateUser() {
    let user = new User;
    user.id = this.user.id;

    var hasChanges: boolean = false;

    if(this.username != null && this.username != this.user.username){
      user.username = this.username;
      hasChanges = true;
    }
    if(this.firstname != null && this.firstname != this.firstname){
      user.firstname = this.firstname;
      hasChanges = true;
    }
    if(this.lastname != null && this.lastname != this.user.lastname){
      user.lastname = this.lastname;
      hasChanges = true;
    }
    if(this.email != null && this.email != this.user.email){
      user.email = this.email;
      hasChanges = true;
    }
    if(this.role != null && this.role != this.user.role){
      user.role = this.role;
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
        error => {
          console.error
          this.username = this.user.username;
        }
      );
    }
  }

}
