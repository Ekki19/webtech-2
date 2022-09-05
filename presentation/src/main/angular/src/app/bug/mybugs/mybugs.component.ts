import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/objects/user';
import { Bug } from 'src/app/objects/bug';
import { SessionService } from 'src/app/services/session.service';
import { BugService } from 'src/app/services/bug.service';

@Component({
  selector: 'wt2-mybugs',
  templateUrl: './mybugs.component.html',
  styleUrls: ['./mybugs.component.sass']
})
export class MybugsComponent implements OnInit {
  public user: User = null;
  public bugs: Bug[] = [];

  constructor(public sessionService: SessionService,
              public bugService: BugService) {


  }

  ngOnInit(): void {
    this.user = this.sessionService.user;

    this.bugService.getAllForUser(this.user.id).subscribe(
      bugs => this.bugs = bugs,
      console.error
    );
  }
}
