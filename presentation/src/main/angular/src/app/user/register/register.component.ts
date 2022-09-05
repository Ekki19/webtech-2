import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { User } from '../../objects/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'wt2-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass']
})
export class RegisterComponent implements OnInit{

  formGroup: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  errorMessage: string = null;
  password: string = "";
  passwordError: boolean = false;
  valPasswordError: boolean = false;
  valPassword: string = "";

  constructor(private sessionService: SessionService,
              private formBuilder: FormBuilder,
              private router: Router) {

  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      username: ['', Validators.required],
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  get check() {
    return this.formGroup.controls;
  }

  register(e: Event) {
    this.submitted = true;
    let passwordError = false;

    this.password = this.password.trim();
    this.valPassword = this.valPassword.trim();

    if(this.password.length > 0 || this.valPassword.length > 0){
      if(this.password != this.valPassword){
        this.passwordError = true;
        this.valPasswordError = true;
        passwordError = true;
      }else {
        this.passwordError = false;
        this.valPasswordError = false;
      }
    }

    if(this.formGroup.invalid || passwordError) {
      return;
    }

    let user = new User();
    user.firstname = this.formGroup.controls['firstname'].value;
    user.lastname = this.formGroup.controls['lastname'].value;
    user.username = this.formGroup.controls['username'].value;
    user.email = this.formGroup.controls['email'].value;
    user.password = this.password;

    this.loading = true;
    this.sessionService.register(user)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(['/login']);
        },
        error => {
          this.errorMessage = "Error while creating User.";
          this.loading = false;
      });
  }
}
