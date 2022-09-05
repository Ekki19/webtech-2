import { Component, OnInit } from '@angular/core';
import { Bug } from 'src/app/objects/bug';
import { SessionService } from 'src/app/services/session.service';
import { BugService } from 'src/app/services/bug.service';

@Component({
  selector: 'wt2-public',
  templateUrl: './publicbugs.component.html',
  styleUrls: ['./publicbugs.component.sass']
})
export class PublicComponent implements OnInit {

  public bugs: Bug[] = [];

  constructor(public sessionService: SessionService,
              public bugService: BugService) {


  }

  ngOnInit(): void {
    this.bugService.getAllPublic().subscribe(
      bugs => this.bugs = bugs,
      console.error
    );
  }
}
