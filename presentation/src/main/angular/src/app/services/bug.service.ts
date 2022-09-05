import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment as env } from '../../environments/environment';
import { map } from 'rxjs/operators';
import { Bug } from '../objects/bug';

@Injectable({
  providedIn: 'root'
})
export class BugService {

  protected defaultHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) { }

  getAllPublic(): Observable<Bug[]> {
    return this.http.get<any[]>(`${env.apiUrl}/rest/bug/all/public`, {headers: this.defaultHeaders}).pipe(
      map(body => body.map(n => Bug.fromObject(n)))
    );
  }

  getAllForUser(id: number): Observable<Bug[]> {
    return this.http.get<any[]>(`${env.apiUrl}/rest/bug?userid=`+id, {headers: this.defaultHeaders}).pipe(
      map(body => body.map(n => Bug.fromObject(n)))
    );
  }

  getBugById(id: number): Observable<Bug> {
    return this.http.get<Bug>(`${env.apiUrl}/rest/bug/`+id, {headers: this.defaultHeaders}).pipe(
        map(body => Bug.fromObject(body))
      );
  }

  create(bug: Bug): Observable<Bug> {
    return this.http.post<Bug>(`${env.apiUrl}/rest/bug/create`, bug, {headers: this.defaultHeaders}).pipe(
      map(body => Bug.fromObject(body))
    );
  }

  delete(id: number): Observable<Bug> {
    return this.http.delete<Bug>(`${env.apiUrl}/rest/bug/delete/`+id, {headers: this.defaultHeaders});
  }

  update(bug: Bug): Observable<Bug> {
    return this.http.post<Bug>(`${env.apiUrl}/rest/bug/update`, bug, {headers: this.defaultHeaders});
  }
}
