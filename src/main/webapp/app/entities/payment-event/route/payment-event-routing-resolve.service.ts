import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentEvent } from '../payment-event.model';
import { PaymentEventService } from '../service/payment-event.service';

@Injectable({ providedIn: 'root' })
export class PaymentEventRoutingResolveService implements Resolve<IPaymentEvent | null> {
  constructor(protected service: PaymentEventService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentEvent | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentEvent: HttpResponse<IPaymentEvent>) => {
          if (paymentEvent.body) {
            return of(paymentEvent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
