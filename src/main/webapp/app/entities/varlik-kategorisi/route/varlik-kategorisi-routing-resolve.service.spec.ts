jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVarlikKategorisi, VarlikKategorisi } from '../varlik-kategorisi.model';
import { VarlikKategorisiService } from '../service/varlik-kategorisi.service';

import { VarlikKategorisiRoutingResolveService } from './varlik-kategorisi-routing-resolve.service';

describe('VarlikKategorisi routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VarlikKategorisiRoutingResolveService;
  let service: VarlikKategorisiService;
  let resultVarlikKategorisi: IVarlikKategorisi | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(VarlikKategorisiRoutingResolveService);
    service = TestBed.inject(VarlikKategorisiService);
    resultVarlikKategorisi = undefined;
  });

  describe('resolve', () => {
    it('should return IVarlikKategorisi returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVarlikKategorisi = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVarlikKategorisi).toEqual({ id: 123 });
    });

    it('should return new IVarlikKategorisi if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVarlikKategorisi = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVarlikKategorisi).toEqual(new VarlikKategorisi());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as VarlikKategorisi })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVarlikKategorisi = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVarlikKategorisi).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
