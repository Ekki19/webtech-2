import { logging } from 'protractor';
import { User } from './user';

export class Comment {

  id: number;
  published: Date;
  comment: string;
  creatorId: number;
  bugId: number;
  creator: User;


  static fromObject(object: any): Comment {
    const comment = new Comment();
    comment.id = object.id;
    comment.published = new Date(object.published)
    comment.comment = object.comment;
    comment.creatorId = object.creatorId;
    comment.bugId = object.bugId;
    comment.creator = object.creator;

    return comment;
  }
}
