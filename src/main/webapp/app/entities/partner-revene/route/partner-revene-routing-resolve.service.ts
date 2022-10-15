import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPartnerRevene } from '../partner-revene.model';
import { PartnerReveneService } from '../service/partner-revene.service';

@Injectable({ providedIn: 'root' })
export class PartnerReveneRoutingResolveService implements Resolve<IPartnerRevene | null> {
  constructor(protected service: PartnerReveneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartnerRevene | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((partnerRevene: HttpResponse<IPartnerRevene>) => {
          if (partnerRevene.body) {
            return of(partnerRevene.body);
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
