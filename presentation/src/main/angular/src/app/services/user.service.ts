import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../objects/user';
import { map } from 'rxjs/operators';
import { environment as env } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  protected defaultHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) { }

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(`${env.apiUrl}/rest/user/all`, {headers: this.defaultHeaders}).pipe(
      map(body => body.map(n => User.fromObject(n)))
    );
  }

  //USER REGISTER IN SESSIONSERVICE INSTEAD!
  /*
  create(user: User): Observable<User> {
    return this.http.post<User>(`${env.apiUrl}/rest/comment/create`, user, {headers: this.defaultHeaders}).pipe(
      map(body => User.fromObject(body))
    );
  }
  */
  update(user: User): Observable<User> {
    return this.http.post<User>(`${env.apiUrl}/rest/user/admin/update`, user, {headers: this.defaultHeaders}).pipe(
      map(body => User.fromObject(body))
    );
  }

  getById(id: number): Observable<User> {
    return this.http.get<User>(`${env.apiUrl}/rest/user/`+id, {headers: this.defaultHeaders}).pipe(
      map(body => User.fromObject(body))
    );
  }
}
