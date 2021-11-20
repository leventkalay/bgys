import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonel, Personel } from '../personel.model';
import { PersonelService } from '../service/personel.service';

@Injectable({ providedIn: 'root' })
export class PersonelRoutingResolveService implements Resolve<IPersonel> {
  constructor(protected service: PersonelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personel: HttpResponse<Personel>) => {
          if (personel.body) {
            return of(personel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Personel());
  }
}
