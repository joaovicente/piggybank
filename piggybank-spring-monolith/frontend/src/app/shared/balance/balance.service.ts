import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BalanceService {

  constructor(private http: HttpClient) { }
  getKidsAndBalances(): Observable<any> {
    return this.http.get(environment.serverUrl + '/kids-and-balances');
  }
}
