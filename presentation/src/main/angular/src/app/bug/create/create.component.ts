import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import { BugService } from 'src/app/services/bug.service';
import { Bug } from 'src/app/objects/bug';
import { User } from '../../objects/user';
import { first } from 'rxjs/operators';

@Component({
  selector: 'wt2-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.sass']
})
export class CreateComponent implements OnInit {

  formGroup: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  errorMessage: string = null;
  bugPublic: boolean = false;
  assignUser: string = "";

  constructor(private sessionService: SessionService,
    private formBuilder: FormBuilder,
    private router: Router,
    private bugService: BugService) {

}

ngOnInit() {
  this.formGroup = this.formBuilder.group({
    headline: ['', Validators.required],
    description: ['', Validators.required]
  });
}

get check() {
  return this.formGroup.controls;
}

publicBug() {
  this.submitted = true;

  if(this.formGroup.invalid) {
    return;
  }

  let user = this.sessionService.user;

  let bug = new Bug();
  bug.creatorId = user.id;
  bug.headline = this.formGroup.controls['headline'].value;
  bug.description = this.formGroup.controls['description'].value;
  bug.created = new Date();
  bug.ispublic = this.bugPublic;

  if(this.assignUser != null) {
    bug.affectedusername = this.assignUser;
  }

  this.loading = true;
    this.bugService.create(bug)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(['/home']);
        },
        error => {
          this.errorMessage = "Error while creating Bug.";
          this.loading = false;
      });
  }


}
