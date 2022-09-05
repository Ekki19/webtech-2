import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Comment } from '../objects/comment';
import { Observable } from 'rxjs';
import { environment as env } from '../../environments/environment';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  protected defaultHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) { }

  getAll(): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${env.apiUrl}/rest/comment/all`, {headers: this.defaultHeaders}).pipe(
      map(body => body.map(n => Comment.fromObject(n)))
    );
  }

  getCommentById(id: number): Observable<Comment> {
    return this.http.get<Comment>(`${env.apiUrl}/rest/comment/`+id, {headers: this.defaultHeaders});
  }

  getCommentsByBugId(id: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${env.apiUrl}/rest/comment/bug/`+id, {headers: this.defaultHeaders}).pipe(
      map(body => body.map(n => Comment.fromObject(n)))
    );
  }

  create(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(`${env.apiUrl}/rest/comment/create`, comment, {headers: this.defaultHeaders}).pipe(
      map(body => Comment.fromObject(body))
    );
  }

  delete(id: number): Observable<Comment> {
    return this.http.delete<Comment>(`${env.apiUrl}/rest/comment/delete/`+id, {headers: this.defaultHeaders});
  }
}
