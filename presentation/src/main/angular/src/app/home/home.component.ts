import { Component, OnInit } from '@angular/core';
import { SessionService } from '../services/session.service';
import { take } from 'rxjs/operators';
import { User } from '../objects/user';
import { BugService } from '../services/bug.service';
import { Bug } from '../objects/bug';

@Component({
  selector: 'wt2-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {

  public user: User = null;
  public userBugs: Bug[] = [];
  public publicBugs: Bug[] = [];

  constructor(public sessionService: SessionService,
              public bugService: BugService) {


  }

  ngOnInit(): void {
    this.sessionService.getUser();
    this.user = this.sessionService.user;
    this.bugService.getAllPublic().subscribe(
      bugs => this.publicBugs = bugs,
      console.error
    );

    this.bugService.getAllForUser(this.user.id).subscribe(
      bugs => this.userBugs = bugs,
      console.error
    );
  }

}
