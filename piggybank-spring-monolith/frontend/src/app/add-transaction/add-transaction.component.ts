import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormControl } from '@angular/forms';
import { TransactionService } from '../shared/transaction/transaction.service';

// see https://angular.io/api/forms/FormControlName#use-with-ngmodel

export class Transaction {
  date: Date;
  kind: string;
  amount: BigInteger;
  description: string;
  kidId: string;
}

@Component({
  selector: 'app-add-transaction',
  templateUrl: './add-transaction.component.html',
  styleUrls: ['./add-transaction.component.css']
})

export class AddTransactionComponent implements OnInit {
  kidId = '';
  sub: Subscription;
  form = new FormGroup({
    kind: new FormControl('CREDIT'),
    description: new FormControl(''),
    date: new FormControl(new Date()),
    amount: new FormControl()
  });

  constructor(private route: ActivatedRoute,
    private router: Router,
    private transactionService: TransactionService) {
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.kidId = params['kidId'];
    });
  }

  onSubmit(): void {
    const transaction = {
      kidId : this.kidId,
      kind : this.form.value.kind,
      date: this.form.value.date,
      amount : this.form.value.amount * 100,
      description: this.form.value.description
    };
    console.log(transaction);
    this.transactionService.create(transaction).subscribe(result => {
      this.gotoTransactionsList();
    }, error => console.error(error));
  }

  gotoTransactionsList() {
    this.router.navigate(['/list-transactions', this.kidId]);
  }
}
