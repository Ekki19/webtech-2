import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../objects/user';

@Component({
  selector: 'wt2-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.sass']
})
export class ManagementComponent implements OnInit {

  public users: User[] = [];

  constructor(private userService: UserService) {

   }

  ngOnInit(): void {
    this.userService.getAll().subscribe(
      users => this.users = users,
      console.error
    );
  }

}
