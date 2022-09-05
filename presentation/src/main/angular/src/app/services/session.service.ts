import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { throwError, Observable, of, BehaviorSubject, observable } from 'rxjs';
import { catchError, map, last, first } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment as env } from '../../environments/environment';
import { User } from '../objects/user';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  public loggedIn: boolean = false;
  public user: User = null;

  protected defaultHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient,
              private router: Router) {
      if(this.user == null) {
        this.getUser();
      }
    }

  login(username: string, password: string): Observable<boolean> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    const headers = new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'});
    return this.http.post(`/login.jsp`, body.toString(), {headers, responseType: 'text'}).pipe(
      map(() => {
        this.loggedIn = true;
        //this.getUser();
        return true;
      }),
      catchError(this.handleError)
    );
  }

  getUser(){
    return this.http.get<User>(`${env.apiUrl}/rest/user/loggedin`, {headers: this.defaultHeaders}).pipe(first())
      .subscribe(response => {
                              //console.log(response);
                              if(response.id != null) {
                                this.loggedIn = true;
                                this.user = response;
                                this.router.navigate(['/home']);
                              } else {
                                this.logout().subscribe();
                                this.loggedIn = false;
                              }
                            }
      );
  }

  logout(): Observable<boolean> {
    return this.http.get(`/logout`).pipe(
      catchError(err => {
        return err.status == 0 ? of([]) : throwError(err);
      }),
      map(() => {
        this.loggedIn = false;
        return true;
      })
    );
  }

  register(user: User): Observable<User> {
    return this.http.post<User>(`${env.apiUrl}/rest/user/new`, user, {headers: this.defaultHeaders}).pipe(
      map(body => User.fromObject(body))
    );
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status},  `+
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  };
}
