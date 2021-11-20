jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISurec, Surec } from '../surec.model';
import { SurecService } from '../service/surec.service';

import { SurecRoutingResolveService } from './surec-routing-resolve.service';

describe('Surec routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SurecRoutingResolveService;
  let service: SurecService;
  let resultSurec: ISurec | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SurecRoutingResolveService);
    service = TestBed.inject(SurecService);
    resultSurec = undefined;
  });

  describe('resolve', () => {
    it('should return ISurec returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSurec = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSurec).toEqual({ id: 123 });
    });

    it('should return new ISurec if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSurec = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSurec).toEqual(new Surec());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Surec })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSurec = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSurec).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
