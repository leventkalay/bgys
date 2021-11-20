import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITehdit, Tehdit } from '../tehdit.model';
import { TehditService } from '../service/tehdit.service';

@Injectable({ providedIn: 'root' })
export class TehditRoutingResolveService implements Resolve<ITehdit> {
  constructor(protected service: TehditService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITehdit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tehdit: HttpResponse<Tehdit>) => {
          if (tehdit.body) {
            return of(tehdit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tehdit());
  }
}
