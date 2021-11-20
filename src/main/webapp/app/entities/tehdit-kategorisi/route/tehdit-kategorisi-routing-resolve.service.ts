import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITehditKategorisi, TehditKategorisi } from '../tehdit-kategorisi.model';
import { TehditKategorisiService } from '../service/tehdit-kategorisi.service';

@Injectable({ providedIn: 'root' })
export class TehditKategorisiRoutingResolveService implements Resolve<ITehditKategorisi> {
  constructor(protected service: TehditKategorisiService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITehditKategorisi> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tehditKategorisi: HttpResponse<TehditKategorisi>) => {
          if (tehditKategorisi.body) {
            return of(tehditKategorisi.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TehditKategorisi());
  }
}
