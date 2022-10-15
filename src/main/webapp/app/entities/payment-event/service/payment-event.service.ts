import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaymentEvent, NewPaymentEvent } from '../payment-event.model';

export type PartialUpdatePaymentEvent = Partial<IPaymentEvent> & Pick<IPaymentEvent, 'id'>;

type RestOf<T extends IPaymentEvent | NewPaymentEvent> = Omit<T, 'on'> & {
  on?: string | null;
};

export type RestPaymentEvent = RestOf<IPaymentEvent>;

export type NewRestPaymentEvent = RestOf<NewPaymentEvent>;

export type PartialUpdateRestPaymentEvent = RestOf<PartialUpdatePaymentEvent>;

export type EntityResponseType = HttpResponse<IPaymentEvent>;
export type EntityArrayResponseType = HttpResponse<IPaymentEvent[]>;

@Injectable({ providedIn: 'root' })
export class PaymentEventService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-events');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentEvent: NewPaymentEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentEvent);
    return this.http
      .post<RestPaymentEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(paymentEvent: IPaymentEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentEvent);
    return this.http
      .put<RestPaymentEvent>(`${this.resourceUrl}/${this.getPaymentEventIdentifier(paymentEvent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(paymentEvent: PartialUpdatePaymentEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentEvent);
    return this.http
      .patch<RestPaymentEvent>(`${this.resourceUrl}/${this.getPaymentEventIdentifier(paymentEvent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPaymentEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPaymentEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaymentEventIdentifier(paymentEvent: Pick<IPaymentEvent, 'id'>): number {
    return paymentEvent.id;
  }

  comparePaymentEvent(o1: Pick<IPaymentEvent, 'id'> | null, o2: Pick<IPaymentEvent, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaymentEventIdentifier(o1) === this.getPaymentEventIdentifier(o2) : o1 === o2;
  }

  addPaymentEventToCollectionIfMissing<Type extends Pick<IPaymentEvent, 'id'>>(
    paymentEventCollection: Type[],
    ...paymentEventsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paymentEvents: Type[] = paymentEventsToCheck.filter(isPresent);
    if (paymentEvents.length > 0) {
      const paymentEventCollectionIdentifiers = paymentEventCollection.map(
        paymentEventItem => this.getPaymentEventIdentifier(paymentEventItem)!
      );
      const paymentEventsToAdd = paymentEvents.filter(paymentEventItem => {
        const paymentEventIdentifier = this.getPaymentEventIdentifier(paymentEventItem);
        if (paymentEventCollectionIdentifiers.includes(paymentEventIdentifier)) {
          return false;
        }
        paymentEventCollectionIdentifiers.push(paymentEventIdentifier);
        return true;
      });
      return [...paymentEventsToAdd, ...paymentEventCollection];
    }
    return paymentEventCollection;
  }

  protected convertDateFromClient<T extends IPaymentEvent | NewPaymentEvent | PartialUpdatePaymentEvent>(paymentEvent: T): RestOf<T> {
    return {
      ...paymentEvent,
      on: paymentEvent.on?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPaymentEvent: RestPaymentEvent): IPaymentEvent {
    return {
      ...restPaymentEvent,
      on: restPaymentEvent.on ? dayjs(restPaymentEvent.on) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPaymentEvent>): HttpResponse<IPaymentEvent> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPaymentEvent[]>): HttpResponse<IPaymentEvent[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
