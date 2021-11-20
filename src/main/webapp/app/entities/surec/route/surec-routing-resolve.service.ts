import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISurec, Surec } from '../surec.model';
import { SurecService } from '../service/surec.service';

@Injectable({ providedIn: 'root' })
export class SurecRoutingResolveService implements Resolve<ISurec> {
  constructor(protected service: SurecService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISurec> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((surec: HttpResponse<Surec>) => {
          if (surec.body) {
            return of(surec.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Surec());
  }
}
