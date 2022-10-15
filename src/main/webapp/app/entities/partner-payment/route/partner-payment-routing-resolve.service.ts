import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPartnerPayment } from '../partner-payment.model';
import { PartnerPaymentService } from '../service/partner-payment.service';

@Injectable({ providedIn: 'root' })
export class PartnerPaymentRoutingResolveService implements Resolve<IPartnerPayment | null> {
  constructor(protected service: PartnerPaymentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartnerPayment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((partnerPayment: HttpResponse<IPartnerPayment>) => {
          if (partnerPayment.body) {
            return of(partnerPayment.body);
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
