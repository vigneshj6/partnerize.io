import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPartnerPayment, NewPartnerPayment } from '../partner-payment.model';

export type PartialUpdatePartnerPayment = Partial<IPartnerPayment> & Pick<IPartnerPayment, 'id'>;

type RestOf<T extends IPartnerPayment | NewPartnerPayment> = Omit<T, 'on'> & {
  on?: string | null;
};

export type RestPartnerPayment = RestOf<IPartnerPayment>;

export type NewRestPartnerPayment = RestOf<NewPartnerPayment>;

export type PartialUpdateRestPartnerPayment = RestOf<PartialUpdatePartnerPayment>;

export type EntityResponseType = HttpResponse<IPartnerPayment>;
export type EntityArrayResponseType = HttpResponse<IPartnerPayment[]>;

@Injectable({ providedIn: 'root' })
export class PartnerPaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/partner-payments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(partnerPayment: NewPartnerPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partnerPayment);
    return this.http
      .post<RestPartnerPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(partnerPayment: IPartnerPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partnerPayment);
    return this.http
      .put<RestPartnerPayment>(`${this.resourceUrl}/${this.getPartnerPaymentIdentifier(partnerPayment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(partnerPayment: PartialUpdatePartnerPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partnerPayment);
    return this.http
      .patch<RestPartnerPayment>(`${this.resourceUrl}/${this.getPartnerPaymentIdentifier(partnerPayment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPartnerPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPartnerPayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPartnerPaymentIdentifier(partnerPayment: Pick<IPartnerPayment, 'id'>): number {
    return partnerPayment.id;
  }

  comparePartnerPayment(o1: Pick<IPartnerPayment, 'id'> | null, o2: Pick<IPartnerPayment, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartnerPaymentIdentifier(o1) === this.getPartnerPaymentIdentifier(o2) : o1 === o2;
  }

  addPartnerPaymentToCollectionIfMissing<Type extends Pick<IPartnerPayment, 'id'>>(
    partnerPaymentCollection: Type[],
    ...partnerPaymentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partnerPayments: Type[] = partnerPaymentsToCheck.filter(isPresent);
    if (partnerPayments.length > 0) {
      const partnerPaymentCollectionIdentifiers = partnerPaymentCollection.map(
        partnerPaymentItem => this.getPartnerPaymentIdentifier(partnerPaymentItem)!
      );
      const partnerPaymentsToAdd = partnerPayments.filter(partnerPaymentItem => {
        const partnerPaymentIdentifier = this.getPartnerPaymentIdentifier(partnerPaymentItem);
        if (partnerPaymentCollectionIdentifiers.includes(partnerPaymentIdentifier)) {
          return false;
        }
        partnerPaymentCollectionIdentifiers.push(partnerPaymentIdentifier);
        return true;
      });
      return [...partnerPaymentsToAdd, ...partnerPaymentCollection];
    }
    return partnerPaymentCollection;
  }

  protected convertDateFromClient<T extends IPartnerPayment | NewPartnerPayment | PartialUpdatePartnerPayment>(
    partnerPayment: T
  ): RestOf<T> {
    return {
      ...partnerPayment,
      on: partnerPayment.on?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPartnerPayment: RestPartnerPayment): IPartnerPayment {
    return {
      ...restPartnerPayment,
      on: restPartnerPayment.on ? dayjs(restPartnerPayment.on) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPartnerPayment>): HttpResponse<IPartnerPayment> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPartnerPayment[]>): HttpResponse<IPartnerPayment[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
