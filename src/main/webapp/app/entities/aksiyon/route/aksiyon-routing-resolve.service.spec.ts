jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAksiyon, Aksiyon } from '../aksiyon.model';
import { AksiyonService } from '../service/aksiyon.service';

import { AksiyonRoutingResolveService } from './aksiyon-routing-resolve.service';

describe('Aksiyon routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AksiyonRoutingResolveService;
  let service: AksiyonService;
  let resultAksiyon: IAksiyon | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AksiyonRoutingResolveService);
    service = TestBed.inject(AksiyonService);
    resultAksiyon = undefined;
  });

  describe('resolve', () => {
    it('should return IAksiyon returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAksiyon = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAksiyon).toEqual({ id: 123 });
    });

    it('should return new IAksiyon if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAksiyon = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAksiyon).toEqual(new Aksiyon());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Aksiyon })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAksiyon = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAksiyon).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
