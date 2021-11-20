jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBirim, Birim } from '../birim.model';
import { BirimService } from '../service/birim.service';

import { BirimRoutingResolveService } from './birim-routing-resolve.service';

describe('Birim routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BirimRoutingResolveService;
  let service: BirimService;
  let resultBirim: IBirim | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BirimRoutingResolveService);
    service = TestBed.inject(BirimService);
    resultBirim = undefined;
  });

  describe('resolve', () => {
    it('should return IBirim returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBirim = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBirim).toEqual({ id: 123 });
    });

    it('should return new IBirim if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBirim = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBirim).toEqual(new Birim());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Birim })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBirim = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBirim).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
