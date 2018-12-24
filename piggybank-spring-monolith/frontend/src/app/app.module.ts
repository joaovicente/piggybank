// npm install -g @angular/cli@7.0.4
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
// npm install @angular/material@7.0.2 @angular/cdk@7.0.2
import { MatTableModule, MatButtonModule, MatIconModule, MatInputModule} from '@angular/material';
import { MatRadioModule, MatDatepickerModule, MatCardModule, MatGridListModule } from '@angular/material';
import { MatNativeDateModule } from '@angular/material';
import { HttpClientModule } from '@angular/common/http';
// npm install time-ago-pipe --save
import { TimeAgoPipe } from 'time-ago-pipe';
// internal project dependencies
import { ListBalancesComponent } from './list-balances/list-balances.component';
import { ListTransactionsComponent } from './list-transactions/list-transactions.component';
import { AddTransactionComponent } from './add-transaction/add-transaction.component';

@NgModule({
  declarations: [
    AppComponent,
    ListBalancesComponent ,
    ListTransactionsComponent,
    AddTransactionComponent,
    TimeAgoPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatTableModule,
    MatCardModule,
    MatGridListModule,
    HttpClientModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatRadioModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatNativeDateModule,
    MatDatepickerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
