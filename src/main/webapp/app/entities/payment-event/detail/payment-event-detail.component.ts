import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentEvent } from '../payment-event.model';

@Component({
  selector: 'jhi-payment-event-detail',
  templateUrl: './payment-event-detail.component.html',
})
export class PaymentEventDetailComponent implements OnInit {
  paymentEvent: IPaymentEvent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentEvent }) => {
      this.paymentEvent = paymentEvent;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
