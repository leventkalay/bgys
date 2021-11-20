import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIsAkisi, IsAkisi } from '../is-akisi.model';
import { IsAkisiService } from '../service/is-akisi.service';

@Injectable({ providedIn: 'root' })
export class IsAkisiRoutingResolveService implements Resolve<IIsAkisi> {
  constructor(protected service: IsAkisiService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIsAkisi> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((isAkisi: HttpResponse<IsAkisi>) => {
          if (isAkisi.body) {
            return of(isAkisi.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IsAkisi());
  }
}
