import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
      // 'Access-Control-Allow-Origin' : '*'
    })
  };
  constructor(private http: HttpClient) { }

  getAll(id): Observable<any> {
    return this.http.get(environment.serverUrl + '/transactions?kidId=' + id);
  }

  create(transaction): Observable<any> {
    const url = environment.serverUrl + '/transactions';
    const body = JSON.stringify(transaction);
    console.log('POST ' + url + body);
    return this.http.post(url, body, this.httpOptions);
  }

  delete(transactionId: string): any {
    const resourceUrl = '/transactions/' + transactionId;
    console.log(resourceUrl);
    return this.http.delete(environment.serverUrl + resourceUrl, this.httpOptions);
  }
}
