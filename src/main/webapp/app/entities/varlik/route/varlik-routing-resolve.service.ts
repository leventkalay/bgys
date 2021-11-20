import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVarlik, Varlik } from '../varlik.model';
import { VarlikService } from '../service/varlik.service';

@Injectable({ providedIn: 'root' })
export class VarlikRoutingResolveService implements Resolve<IVarlik> {
  constructor(protected service: VarlikService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVarlik> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((varlik: HttpResponse<Varlik>) => {
          if (varlik.body) {
            return of(varlik.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Varlik());
  }
}
