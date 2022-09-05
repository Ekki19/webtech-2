export class User {

  id: number;
  username: string;
  firstname: string;
  lastname: string;
  password: string;
  email: string;
  registered: Date;
  role: string;

  static fromObject(object: any): User {
    const user = new User();
    user.id = object.id;
    user.username = object.username;
    user.firstname = object.firstname;
    user.lastname = object.lastname;
    user.email = object.email;
    user.registered = new Date(object.registered)
    user.role = object.role;

    return user;
  }
}
