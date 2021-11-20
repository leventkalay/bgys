jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRisk, Risk } from '../risk.model';
import { RiskService } from '../service/risk.service';

import { RiskRoutingResolveService } from './risk-routing-resolve.service';

describe('Risk routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RiskRoutingResolveService;
  let service: RiskService;
  let resultRisk: IRisk | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RiskRoutingResolveService);
    service = TestBed.inject(RiskService);
    resultRisk = undefined;
  });

  describe('resolve', () => {
    it('should return IRisk returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRisk = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRisk).toEqual({ id: 123 });
    });

    it('should return new IRisk if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRisk = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRisk).toEqual(new Risk());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Risk })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRisk = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRisk).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
