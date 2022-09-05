import { Component, OnInit, AfterContentInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BugService } from 'src/app/services/bug.service';
import { Bug } from 'src/app/objects/bug';
import { Comment } from 'src/app/objects/comment';
import { CommentService } from 'src/app/services/comment.service';
import { User } from 'src/app/objects/user';
import { SessionService } from 'src/app/services/session.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'wt2-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.sass']
})
export class DetailsComponent {

  public bug: Bug = new Bug();
  public comments: Comment[] = [];
  public id: number = -1;
  public errorMessage: string = null;
  public loading: boolean = false;
  public user: User = null;
  formGroup: FormGroup;
  formGroupUpdate: FormGroup;
  public submitted: boolean = false;
  public showBugInfo: boolean = true;
  public showEditBug: boolean = false;
  bugPublic: boolean = false;
  assignUser: string = "";

  constructor(private route: ActivatedRoute,
              private bugService: BugService,
              private router: Router,
              private commenService: CommentService,
              private sessionService: SessionService,
              private formBuilder: FormBuilder) {

  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      usercomment: ['', Validators.required]
    });

    this.formGroupUpdate = this.formBuilder.group({
      headline: [this.bug.headline, Validators.required],
      description: [this.bug.description, Validators.required]
    });

    this.id = +this.route.snapshot.paramMap.get('id');

    this.getBug();
    this.getComments();

    this.user = this.sessionService.user;
  }

  deleteBug() {
    var confirm = window.confirm("Do you really want to delete? This cannot be undun!");
    if(confirm){
      this.bugService.delete(this.bug.id).subscribe(
        resp => this.router.navigate(['/home'])
      );
    }
  }

  deleteComment(id: number) {
    var confirm = window.confirm("Do you really want to delete? This cannot be undun!");
    if(confirm){
      this.commenService.delete(id).subscribe(
        resp =>  this.getComments(),
      );
    }
  }
  getBug() {
    this.bugService.getBugById(this.id).subscribe(
      bug => {
        this.bug = bug;
        this.formGroupUpdate.controls.headline.setValue(this.bug.headline);
        this.formGroupUpdate.controls.description.setValue(this.bug.description);
        this.assignUser = this.bug.affectedusername;
        this.bugPublic = this.bug.ispublic;
      },
      console.error
    );
  }

  getComments() {
    this.commenService.getCommentsByBugId(this.id).subscribe(
      comments => this.comments = comments,
      console.error
    );
  }

  updateBug() {
    let bug = new Bug();

    bug.id = this.bug.id;
    bug.headline = this.formGroupUpdate.controls['headline'].value;
    bug.description = this.formGroupUpdate.controls['description'].value;
    bug.ispublic = this.bugPublic;

    if(this.assignUser != null && this.assignUser != "") {
      bug.affectedusername = this.assignUser;
    }

    this.bugService.update(bug).subscribe(
      bug => this.bug = bug,
      console.error
    );

    this.showEditForm();
  }

  isAdmin() {
    let role = this.user.role;
    return role === "admin";
  }

  isAllowedToEdit() {
    let role = this.user.role;
    return this.user.id === this.bug.creatorId;
  }

  isAllowedToDelete() {
    let role = this.user.role;
    return role === "admin" || this.user.id === this.bug.creatorId;
  }

  isAllowedToComment() {
    return this.user.role === "moderator" || this.user.role === "developer" || this.user.role === "admin";
  }

  get check() {
    return this.formGroup.controls;
  }

  createComment() {
    this.submitted = true;

    if(this.formGroup.invalid) {
      return;
    }

    let comment = new Comment();
    comment.comment = this.formGroup.controls['usercomment'].value;
    comment.creatorId = this.user.id;
    comment.published = new Date();
    comment.bugId = this.id;

    this.loading = true;

    /*router.navigate(['/details/'+this.id]);*/
    this.commenService.create(comment).subscribe(
      data => {
        this.getComments();
        this.formGroup.controls["usercomment"].setValue("");
      },
      error => {
        this.errorMessage = "Error while creating Bug.";
        this.loading = false;
    });
    //this.getComments();
  }

  showEditForm() {
    this.showBugInfo = !this.showBugInfo;
    this.showEditBug = !this.showEditBug;
  }
}
