import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIsAkisi, getIsAkisiIdentifier } from '../is-akisi.model';

export type EntityResponseType = HttpResponse<IIsAkisi>;
export type EntityArrayResponseType = HttpResponse<IIsAkisi[]>;

@Injectable({ providedIn: 'root' })
export class IsAkisiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/is-akisis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(isAkisi: IIsAkisi): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isAkisi);
    return this.http
      .post<IIsAkisi>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(isAkisi: IIsAkisi): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isAkisi);
    return this.http
      .put<IIsAkisi>(`${this.resourceUrl}/${getIsAkisiIdentifier(isAkisi) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(isAkisi: IIsAkisi): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isAkisi);
    return this.http
      .patch<IIsAkisi>(`${this.resourceUrl}/${getIsAkisiIdentifier(isAkisi) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIsAkisi>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIsAkisi[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIsAkisiToCollectionIfMissing(isAkisiCollection: IIsAkisi[], ...isAkisisToCheck: (IIsAkisi | null | undefined)[]): IIsAkisi[] {
    const isAkisis: IIsAkisi[] = isAkisisToCheck.filter(isPresent);
    if (isAkisis.length > 0) {
      const isAkisiCollectionIdentifiers = isAkisiCollection.map(isAkisiItem => getIsAkisiIdentifier(isAkisiItem)!);
      const isAkisisToAdd = isAkisis.filter(isAkisiItem => {
        const isAkisiIdentifier = getIsAkisiIdentifier(isAkisiItem);
        if (isAkisiIdentifier == null || isAkisiCollectionIdentifiers.includes(isAkisiIdentifier)) {
          return false;
        }
        isAkisiCollectionIdentifiers.push(isAkisiIdentifier);
        return true;
      });
      return [...isAkisisToAdd, ...isAkisiCollection];
    }
    return isAkisiCollection;
  }

  protected convertDateFromClient(isAkisi: IIsAkisi): IIsAkisi {
    return Object.assign({}, isAkisi, {
      sonTarih: isAkisi.sonTarih?.isValid() ? isAkisi.sonTarih.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.sonTarih = res.body.sonTarih ? dayjs(res.body.sonTarih) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((isAkisi: IIsAkisi) => {
        isAkisi.sonTarih = isAkisi.sonTarih ? dayjs(isAkisi.sonTarih) : undefined;
      });
    }
    return res;
  }
}
