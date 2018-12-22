import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListBalancesComponent } from './list-balances/list-balances.component';
import { ListTransactionsComponent } from './list-transactions/list-transactions.component';
import { AddTransactionComponent } from './add-transaction/add-transaction.component';

const routes: Routes = [
  { path: '', redirectTo: 'list-balances', pathMatch: 'full' },
  { path: 'list-balances' , component: ListBalancesComponent },
  { path: 'list-transactions/:id', component: ListTransactionsComponent },
  { path: 'add-transaction/:kidId', component: AddTransactionComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
