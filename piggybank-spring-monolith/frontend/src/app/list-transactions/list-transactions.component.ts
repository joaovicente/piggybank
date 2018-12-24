import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { TransactionService} from '../shared/transaction/transaction.service';

@Component({
  selector: 'app-list-transactions',
  templateUrl: './list-transactions.component.html',
  styleUrls: ['./list-transactions.component.css']
})
export class ListTransactionsComponent implements OnInit {
  id = '';
  sub: Subscription;
  displayedColumns: string[] = ['date', 'kind', 'amount', 'description', 'buttons' ];
  transactions: any;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private transactionService: TransactionService) {
}

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.id = params['id'];
      this.getTransactions();
    });
  }

  toEuro(centAmount)  {
    return centAmount / 100;
  }

  isCredit(kind)  {
    return (kind === 'CREDIT');
  }

  onDelete(transactionId): void {
    this.transactionService.delete(transactionId).subscribe(result => {
      this.getTransactions();
    }, error => console.error(error));
  }

  getTransactions() {
    this.transactionService.getAll(this.id).subscribe(data => {
      this.transactions = data;
    });
  }
}
