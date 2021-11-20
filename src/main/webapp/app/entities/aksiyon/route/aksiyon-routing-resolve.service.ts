import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAksiyon, Aksiyon } from '../aksiyon.model';
import { AksiyonService } from '../service/aksiyon.service';

@Injectable({ providedIn: 'root' })
export class AksiyonRoutingResolveService implements Resolve<IAksiyon> {
  constructor(protected service: AksiyonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAksiyon> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aksiyon: HttpResponse<Aksiyon>) => {
          if (aksiyon.body) {
            return of(aksiyon.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Aksiyon());
  }
}
