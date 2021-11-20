import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVarlikKategorisi, VarlikKategorisi } from '../varlik-kategorisi.model';
import { VarlikKategorisiService } from '../service/varlik-kategorisi.service';

@Injectable({ providedIn: 'root' })
export class VarlikKategorisiRoutingResolveService implements Resolve<IVarlikKategorisi> {
  constructor(protected service: VarlikKategorisiService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVarlikKategorisi> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((varlikKategorisi: HttpResponse<VarlikKategorisi>) => {
          if (varlikKategorisi.body) {
            return of(varlikKategorisi.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VarlikKategorisi());
  }
}
