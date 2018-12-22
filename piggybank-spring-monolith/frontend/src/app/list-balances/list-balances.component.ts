import { Component, OnInit } from '@angular/core';
import { BalanceService } from '../shared/balance/balance.service';

@Component({
  selector: 'app-list-balances',
  templateUrl: './list-balances.component.html',
  styleUrls: ['./list-balances.component.css']
})
export class ListBalancesComponent implements OnInit {
  displayedColumns: string[] = ['kidName', 'kidBalance', 'buttons' ];
  kidsAndBalances: any;

  constructor(private balanceService: BalanceService) { }

  ngOnInit() {
    this.balanceService.getKidsAndBalances().subscribe(data => {
      this.kidsAndBalances = data;
    });
  }
  toEuro(centAmount)  {
    return centAmount / 100;
  }
}
