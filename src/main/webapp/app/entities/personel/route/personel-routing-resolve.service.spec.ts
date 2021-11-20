jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonel, Personel } from '../personel.model';
import { PersonelService } from '../service/personel.service';

import { PersonelRoutingResolveService } from './personel-routing-resolve.service';

describe('Personel routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PersonelRoutingResolveService;
  let service: PersonelService;
  let resultPersonel: IPersonel | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PersonelRoutingResolveService);
    service = TestBed.inject(PersonelService);
    resultPersonel = undefined;
  });

  describe('resolve', () => {
    it('should return IPersonel returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPersonel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPersonel).toEqual({ id: 123 });
    });

    it('should return new IPersonel if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPersonel = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPersonel).toEqual(new Personel());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Personel })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPersonel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPersonel).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
