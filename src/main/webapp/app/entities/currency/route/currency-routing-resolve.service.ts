import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICurrency } from '../currency.model';
import { CurrencyService } from '../service/currency.service';

@Injectable({ providedIn: 'root' })
export class CurrencyRoutingResolveService implements Resolve<ICurrency | null> {
  constructor(protected service: CurrencyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICurrency | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((currency: HttpResponse<ICurrency>) => {
          if (currency.body) {
            return of(currency.body);
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
