import { User } from './user';

export class Bug {

  id: number;
  created: Date;
  description: string;
  headline: string;
  creatorId: number;
  commentListIds: number[]
  ispublic: boolean;
  affecteduserid: number;
  affectedusername: string;
  creator: User;

  static fromObject(object: any): Bug {
    const bug = new Bug();
    bug.id = object.id;
    bug.created = new Date(object.created)
    bug.description = object.description;
    bug.headline = object.headline;
    bug.creatorId = object.creatorId;
    bug.commentListIds = object.commentListIds;
    bug.ispublic = object.ispublic;
    bug.affecteduserid = object.affecteduserid;
    bug.affectedusername = object.affectedusername;
    bug.creator = object.creator;

    return bug;
  }

}
