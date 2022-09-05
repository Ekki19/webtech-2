import { Component, Input } from '@angular/core';
import { Bug } from '../../objects/bug';

@Component({
  selector: 'wt2-bug-list',
  templateUrl: './bug-list.component.html',
  styleUrls: ['./bug-list.component.sass']
})
export class BugListComponent {

  @Input()
  public showShowMoreLink: boolean = true;

  @Input()
  public isPublic: boolean = false;

  @Input()
  public bugs: Bug[] = [];

  get reversedBugs(): Bug[] {
    return this.bugs.slice().reverse();
  }
}
