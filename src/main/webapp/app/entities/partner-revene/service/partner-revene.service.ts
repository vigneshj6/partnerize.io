import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPartnerRevene, NewPartnerRevene } from '../partner-revene.model';

export type PartialUpdatePartnerRevene = Partial<IPartnerRevene> & Pick<IPartnerRevene, 'id'>;

export type EntityResponseType = HttpResponse<IPartnerRevene>;
export type EntityArrayResponseType = HttpResponse<IPartnerRevene[]>;

@Injectable({ providedIn: 'root' })
export class PartnerReveneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/partner-revenes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(partnerRevene: NewPartnerRevene): Observable<EntityResponseType> {
    return this.http.post<IPartnerRevene>(this.resourceUrl, partnerRevene, { observe: 'response' });
  }

  update(partnerRevene: IPartnerRevene): Observable<EntityResponseType> {
    return this.http.put<IPartnerRevene>(`${this.resourceUrl}/${this.getPartnerReveneIdentifier(partnerRevene)}`, partnerRevene, {
      observe: 'response',
    });
  }

  partialUpdate(partnerRevene: PartialUpdatePartnerRevene): Observable<EntityResponseType> {
    return this.http.patch<IPartnerRevene>(`${this.resourceUrl}/${this.getPartnerReveneIdentifier(partnerRevene)}`, partnerRevene, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPartnerRevene>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPartnerRevene[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPartnerReveneIdentifier(partnerRevene: Pick<IPartnerRevene, 'id'>): number {
    return partnerRevene.id;
  }

  comparePartnerRevene(o1: Pick<IPartnerRevene, 'id'> | null, o2: Pick<IPartnerRevene, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartnerReveneIdentifier(o1) === this.getPartnerReveneIdentifier(o2) : o1 === o2;
  }

  addPartnerReveneToCollectionIfMissing<Type extends Pick<IPartnerRevene, 'id'>>(
    partnerReveneCollection: Type[],
    ...partnerRevenesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partnerRevenes: Type[] = partnerRevenesToCheck.filter(isPresent);
    if (partnerRevenes.length > 0) {
      const partnerReveneCollectionIdentifiers = partnerReveneCollection.map(
        partnerReveneItem => this.getPartnerReveneIdentifier(partnerReveneItem)!
      );
      const partnerRevenesToAdd = partnerRevenes.filter(partnerReveneItem => {
        const partnerReveneIdentifier = this.getPartnerReveneIdentifier(partnerReveneItem);
        if (partnerReveneCollectionIdentifiers.includes(partnerReveneIdentifier)) {
          return false;
        }
        partnerReveneCollectionIdentifiers.push(partnerReveneIdentifier);
        return true;
      });
      return [...partnerRevenesToAdd, ...partnerReveneCollection];
    }
    return partnerReveneCollection;
  }
}
