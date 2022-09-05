import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/objects/user';

@Component({
  selector: 'wt2-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.sass']
})
export class UserListComponent implements OnInit {

  @Input()
  public users: User[] = [];

  constructor() { }

  ngOnInit(): void {
  }

}
