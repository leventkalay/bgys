jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVarlik, Varlik } from '../varlik.model';
import { VarlikService } from '../service/varlik.service';

import { VarlikRoutingResolveService } from './varlik-routing-resolve.service';

describe('Varlik routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VarlikRoutingResolveService;
  let service: VarlikService;
  let resultVarlik: IVarlik | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(VarlikRoutingResolveService);
    service = TestBed.inject(VarlikService);
    resultVarlik = undefined;
  });

  describe('resolve', () => {
    it('should return IVarlik returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVarlik = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVarlik).toEqual({ id: 123 });
    });

    it('should return new IVarlik if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVarlik = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVarlik).toEqual(new Varlik());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Varlik })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVarlik = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVarlik).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
